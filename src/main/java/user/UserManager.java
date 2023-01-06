package user;

import network.Input;
import network.ReceiverThread;
import network.TransmitterThread;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class UserManager {

    private ArrayList<User> users = new ArrayList<>();
    private Input scanner;
    private InetAddress notreIP;
    //private InetAddress notreIP;

    public UserManager () throws IOException {

        //Début notifications broadcast pour la connexion

        //Fin notifications broadcast pour la connexion
         this.scanner = Input.getInstance();

    }

    public String getIP(){
        // Ce code permet de récuperer notre IPv4. Le simple getHostAddress ne fonctionne pas sur les PC de l'INSA
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while( networkInterfaceEnumeration.hasMoreElements()){
                for ( InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses())
                    if ( interfaceAddress.getAddress().isSiteLocalAddress()) {
                        this.notreIP = InetAddress.getByName(interfaceAddress.getAddress().getHostAddress());
                    }

            }
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return notreIP.toString().substring(1);
    }


    public void update(List<String> dataList) throws IOException {

        List<String> newData =  new ArrayList<String>(dataList);
        //this.udpThread.clearData(); //Remise à 0 de la liste.
        for(int i = 0; i < newData.size() ; i++)
        {
            // data[0] --> type de message (ex: "n", "m") | data [1] --> pseudo | data[2] --> @IP
            String data[] = newData.get(i).split("/"); // On split les informations que l'on reçoit dans le paquet pour les récupérer par la suite
            InetAddress ipAddress = InetAddress.getByName(data[2]); // Conversion de l'addresse ip reçue de String en InetAddress

            // Lorsque l'on reçoit un message de connexion de la part d'un autre user
            if(data[0].equals("c"))
            {
                //On vérifie que cette personne a choisi un pseudo unique en se basant sur notre liste
                if(this.checkUser(data[1], ipAddress)){ //checkUser enverra un message "w" si le pseudo est déjà utilisé et n'ira pas plus loin dans le if
                    System.out.println(this.createUser(data[1], ipAddress));
                    System.out.println(data[1]);
                    this.sendUDP("n",data[2],null); // message pour notifier le nouvel utilisateur de notre présence afin qu'il nous ajoute à sa liste d'utilisateurs
                }
            }
            else if (data[0].equals("d")) { //C'est une déconnexion
                System.out.println(this.deleteUser(data[1], ipAddress));
            }
            else if (data[0].equals("m")) { //On reçoit un paquet de quelqu'un souhaitant changer de pseudo
                if(this.checkUser(data[1], ipAddress)){ //On vérifie qu'il n'y a pas d'autres personnes possèdant ce pseudo
                    for(User n : users)  //Si c'est le cas, on parcourt la liste des users dans notre liste
                        if (n.getIpAddress().equals(data[2])){ //On retrouve l'utilisateur souhaitant changer de pseudo grâce à son @IP
                            n.setPseudo(data[1]); //On lui met le nouveau pseudo
                            System.out.println("SUCCESS ---- The pseudo has been changed");
                            this.sendUDP("g",data[2],data[1]); //On le notifie que tout est ok pour nous
                        }
                }
                else { //Si le pseudo est déjà pris
                    this.sendUDP("w",data[2]); //On notifie l'utilisateur que le pseudo est déjà pris
                }
            }
            else if (data[0].equals("w")) { //Réception d'un message des autres users pour notifier que le pseudo existe déjà dans leur liste de contact
                System.out.println("ERROR ---- Please choose another Pseudo, this one is already used");
                this.sendUDP("m");
            }
            else if (data[0].equals("g")) { //Réception d'un message des autres users pour notifier que le pseudo n'existe pas dans leur liste de contact
                System.out.println("SUCCESS ---- The chosen pseudo is unique");
                for(User n : users)  //Si c'est le cas, on parcourt la liste des users
                    if (n.getIpAddress().equals(this.users.get(0).getIpAddress())){ //On retrouve notre profil grâce à notre @IP
                        n.setPseudo(data[1]); //On change notre pseudo par le nouveau choisi et reçu dans le paquet "g"
                        System.out.println("SUCCESS ---- Your pseudo has been changed");
                    }
            }
            else if(data[0].equals("n")) { // Réception de ce message de la part d'utilisateurs déjà présents dans le chat system
                System.out.println(this.createUser(data[1], ipAddress)); // mise à jour de la liste d'utilisateurs en conséquence
                System.out.println(data[1]);
            }
        }
    }

    public boolean checkUser(String pseudo, InetAddress ipAddress) throws IOException{
        for(User n : users)
            if (n.getPseudo().equals(pseudo)){
                //String message = pseudo +ipAddress;
                this.sendUDP("w", ipAddress.toString());
                //System.out.println("ERROR ---- The pseudo is already used.");
                return false;
            } else {
                this.sendUDP("g", ipAddress.toString());
                //System.out.println("SUCCESS ---- This pseudo is unique.");
            }
        return true;
    }

    // Méthode permettant d'ajouter un utilisateur à la liste "users" et qui return un message confirmant l'ajout de cet utilisateur
    public String createUser(String pseudo, InetAddress ipAddress) throws IOException {
        //Conversion de l'addresse IP en InetAddress
        User newUser = new User(pseudo, ipAddress); //Création d'un new user

        users.add(newUser); //Ajout de l'user a la liste

        // Boucle pour parcourir la liste et vérifier que l'utilisateur a bien été ajouté
        for(User n : users)
            if (n.getPseudo().equals(newUser.getPseudo())){
                return "SUCCESS ---- User : " + newUser.getPseudo() + " with @IP = " + newUser.getIpAddress() + " has SUCCESSFULLY been added to the list of users";
        }
        return "ERROR ---- User : " + newUser.getPseudo() + " with @IP = " + newUser.getIpAddress() + " has NOT been added to the list of users";
    }

    // Méthode permettant de supprimer un utilisateur de la liste "users" et return un message confirmant la suppression
    public String deleteUser(String pseudo, InetAddress ipAddress) {

        users.removeIf(user -> user.getPseudo().equals(pseudo) &&  user.getIpAddress().equals(ipAddress));

        for(User n : users)
            if (n.getPseudo().equals(pseudo)){
                return "ERROR ---- User : " + pseudo + " with @IP = " + ipAddress + " has NOT been deleted from the list of users";
            }
        return "SUCCESS ---- User : " + pseudo + " with @IP = " + ipAddress + " has been deleted from the list of users";
    }

    //Méthode pour se connecter à un serveur TCP
    public String sendTCP(String type, String ... ip) throws IOException {
        String ipAddress = ip.length > 0 ? ip[0] : null ;

        if(type.equals("s")){ //Démarrage d'une conversation
            String serverAddress = "10.1.5.81";
            int port = 4000; //numéro de port du serveur
            Socket socket = new Socket(serverAddress, port); //création du socket avec comme paramètres les variables créées ci-dessus

            // On lance les threads d'échange afin d'envoyer et recevoir des messages
            TransmitterThread transmit = null; //Pour chaque client connecté on crée un Thread qui va gérer les communications
            transmit = new TransmitterThread(socket);
            transmit.start(); //On lance le Thread (--> run() dans TransmitterThread)

            ReceiverThread runnableReceive = null;
            Thread receive = new Thread(runnableReceive);
            receive.start(); //On lance le Thread (--> run() dans ReceiverThread)
            return "connexion établie";
        }

        return "connexion impossible";
    }

        public String sendUDP(String type, String ... data) throws IOException {
        String ipAddress = data.length > 0 ? data[0] : null ; //S'il n'y a pas d'@IP --> ipAddress vaut null
        String pseudoReceived = data[1]; //S'il n'y a pas de pseudo --> pseudoReceived vaut null
        if(type.equals("c")) {
            // Create a Scanner object
            System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur
            String pseudo = scanner.getNextLine();  //Lecture de l'entrée utilisateur;
            this.createUser(pseudo, InetAddress.getByName("127.0.0.1"));
            createDatagramUDP(pseudo, "10.1.255.255", "c");
            return "Connection successful";


        } else if (type.equals("d")) { //Envoi d'un broadcast pour faire part de notre déconnexion aux autres utilisateurs et qu'ils mettent leur liste à jour
            this.createDatagramUDP(users.get(0).getPseudo(),"10.1.255.255", "d");
            return "logout successful";

        } else if (type.equals("m")) { //Envoi d'un changement de pseudo avec notre nouveau pseudo à l'intérieur du paquet
            System.out.println("Rentrez votre nouveau pseudo : ");
            String newPseudo = scanner.getNextLine();
            this.createDatagramUDP(newPseudo, "10.1.255.255", "m");
            return "Changing pseudo successful";

        } else if(type.equals("w")) { //Réponse à un message "m" lorsqu'une personne a pris un pseudo déjà utilisé.
            this.createDatagramUDP(null, ipAddress.substring(1), "w"); // substring pour enlever le premier "/"

        } else if(type.equals("g")) { //Réponse à un message "m" lorsqu'une personne a pris un pseudo unique afin de lui confirmer l'unicité de celui-ci
            //On met dand le paquet le pseudo que l'utilisateur souhaiter pour prendre pour qu'il puisse l'update de son côté
            this.createDatagramUDP(pseudoReceived, ipAddress.substring(1), "g");

        } else if(type.equals("n")) { //Envoi de ce message pour notifier le nouvel utilisateur de notre présence afin qu'il mette sa liste d'utilisateurs à jour
            System.out.println(ipAddress.substring(1));
            this.createDatagramUDP(this.users.get(0).getPseudo(), ipAddress.substring(1), "n");
            return "Notification successful";
        }
        return "Fail sending UDP Packet";
    }

    public int createDatagramUDP(String pseudo, String ipAddr, String message) throws IOException {
        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket
        String payload = message + "/" + pseudo; //Création du payload
        InetAddress destination = InetAddress.getByName(ipAddr); //Adresse destination
        DatagramPacket outPacket = new DatagramPacket(payload.getBytes(), payload.length(), destination, 4445); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi du datagramme
        dgramSocket.close(); //Fermeture du socket
        return 0;
    }

    public ArrayList<User> getUsers()
    {
        return this.users;
    }

    public Input getScanner() {return this.scanner; }
}



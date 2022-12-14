package user;

import network.Input;
import user.User;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private ArrayList<User> users = new ArrayList<>();
    private Input scanner;

    public UserManager () throws IOException {

        //Début notifications broadcast pour la connection

        //Fin notifications broadcast pour la connection
         this.scanner = Input.getInstance();

    }


    public void update(List<String> dataList) throws IOException {

        List<String> newData =  new ArrayList<String>(dataList);
        //this.udpThread.clearData(); //Remise à 0 de la liste.
        for(int i = 0; i < newData.size() ; i++)
        {
            String data[] = newData.get(i).split("/"); // On split les informations que l'on reçoit dans le paquet pour les récupérer par la suite
            InetAddress ipAddress = InetAddress.getByName(data[2]); // Conversion de l'addresse ip recu de String en InetAddress

            if(data[0].equals("c")) // Lors de la connexion
            {
                if(this.checkUser(data[1], ipAddress)){
                    System.out.println(this.createUser(data[1], ipAddress));
                    System.out.println(data[1]);
                    this.sendUDP("n");
                }
            }
            else if (data[0].equals("d")) { // C'est une déconnexion
                System.out.println(this.deleteUser(data[1], ipAddress));
            }
            else if (data[0].equals("m")) { //C'est un changement de pseudo
                if(this.checkUser(data[1], ipAddress)){
                    for(User n : users) //On change le pseudo
                        if (n.getIpAddress().equals(data[2])){
                            n.setPseudo(data[1]);
                            System.out.println("SUCCESS ---- The pseudo has been changed");
                        }
                }
            }
            else if (data[0].equals("w")) { // Réception d'un message des autres users pour notifier que le pseudo existe déjà dans leur liste de contact
                System.out.println("ERROR ---- Please choose another Pseudo, this one is already used");
                this.sendUDP("m");
            }
            else if (data[0].equals("g")) { // Réception d'un message des autres users pour notifier que le pseudo n'existe pas dans leur liste de contact
                System.out.println("SUCCESS ---- The pseudo is unique");
            }
            else if(data[0].equals("n")) { // Réception d'un message d'un nouvel utilisateur arrivant dans le chat system
                System.out.println(this.createUser(data[1], ipAddress)); // mise à jour de la liste d'utilisateurs en conséquence
                System.out.println(data[1]);
            }
        }
    }

    public boolean checkUser(String pseudo, InetAddress ipAddress) throws IOException{
        for(User n : users)
            if (n.getPseudo().equals(pseudo)){
                String message = pseudo +ipAddress;
                this.sendUDP("w", ipAddress.toString());
                System.out.println("ERROR ---- The pseudo is already used.");
                return false;
            } else {
                this.sendUDP("g", ipAddress.toString());
                System.out.println("SUCCESS ---- This pseudo is unique.");
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

    public String sendUDP(String type, String ... ip) throws IOException {
        String ipAddress = ip.length > 0 ? ip[0] : null ;
        if(type.equals("c")) {

            // Create a Scanner object
            System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur
            String pseudo = scanner.getNextLine();  //Lecture de l'entrée utilisateur;
            this.createUser(pseudo, InetAddress.getByName("127.0.0.1"));
            createDatagramUDP(pseudo, "10.1.255.255", "c");
            return "Connection successful";

        } else if (type.equals("d")) { //Envoi d'un broadcast de deconnexion
            this.createDatagramUDP(users.get(0).getPseudo(),"10.1.255.255", "d");
            return "logout successful";

        } else if (type.equals("m")) { //Envoi d'un changement de pseudo
            System.out.println("Rentrez votre nouveau pseudo : ");
            String newPseudo = scanner.getNextLine();
            this.createDatagramUDP(newPseudo, "10.1.255.255", "m");
            return "Changing pseudo successful";

        } else if(type.equals("w")) { //Wrong pseudo, on envoi ce paquet si la personne a prit un pseudo deja utilisé.
            this.createDatagramUDP(null, ipAddress.substring(1), "w"); // substring pour enlever le premier "/"

        } else if(type.equals("g")) {
            this.createDatagramUDP(null, ipAddress.substring(1), "g");

        } else if(type.equals("n")) { // Message des autres utilisateurs pour annoncer leur existence afin que le nouvel utilisateur mette sa liste à jour
            this.createDatagramUDP(this.users.get(0).getPseudo(), ipAddress.substring(1), "n");
            return "Notification successful";
        }
        return "Fail sending UDP Packet";
    }

    public int createDatagramUDP(String pseudo, String ipAddr, String message) throws IOException {
        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
        String payload = message + "/" + pseudo; //Création du payload du paquet UDP
        InetAddress broadcast = InetAddress.getByName(ipAddr); //Adresse destination !!Doit etre un broadcast !!
        DatagramPacket outPacket = new DatagramPacket(payload.getBytes(), payload.length(), broadcast, 4445); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi de la notification de connexion
        dgramSocket.close();
        return 0;
    }

    public ArrayList<User> getUsers()
    {
        return this.users;
    }

    public Input getScanner() {return this.scanner; }


}



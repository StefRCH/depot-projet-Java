package network;

import user.UserManager;

import java.io.IOException;


/**
 * Hello world!
 *
 */
public class App
{

    public static void main( String[] args ) throws IOException, InterruptedException {
        //Lancement du serveur TCP pour accepter les requêtes entrantes
        TCPNetworkManager tcpNetworkManager = new TCPNetworkManager();
        tcpNetworkManager.start();

        //Lancement d'UDP
        UDPThread udpThread = new UDPThread();
        udpThread.start();

        //Création de l'User Manager au lancement de l'application
        UserManager userManager = udpThread.getUserManager();
        userManager.sendUDPConnexion();


        /*String pseudo = myObj.nextLine();  //Lecture de l'entrée utilisateur;
        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
        String message = "c/" +pseudo; //Création du payload du paquet UDP
        InetAddress broadcast = InetAddress.getByName("127.0.0.1"); //Adresse destination !!Doit etre un broadcast !!
        int port = 4445; //Port de destination du broadcast
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi de la notification de connexion
        dgramSocket.close(); */

        Input scanner = userManager.getScanner();
        while (true) {
            System.out.println("Pour vous deconnecter taper d, pour changer de pseudo taper m, pour initier une conversation taper s, pour une belle surprise taper g");
            String userPrompt = scanner.getNextLine();  //Lecture de l'entrée utilisateur;
            if (userPrompt.equals("d")) {
                userManager.sendUDPDeconnexion();
            } else if (userPrompt.equals("m")) {
                userManager.sendUDPChangePseudo();
            } else if(userPrompt.equals("s")){
                userManager.sendTCP("s");
            }




            else if (userPrompt.equals("g")){
                System.out.println("Eh belle bite !");
            }
        }
    }
}


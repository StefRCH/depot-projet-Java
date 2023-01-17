package network;

import user.UserManager;

import java.io.IOException;


/**
 * Cette classe est celle de lancement, elle lance le serveur TCP, la "lecture" des paquets UDP et read les input de base de l'utilisateur
 */
public class App {
    public static int x = 0;
    public static void main( String[] args ) throws IOException, InterruptedException {
        //Lancement du serveur TCP pour accepter les requêtes entrantes
        TCPNetworkManager tcpNetworkManager = new TCPNetworkManager();
        tcpNetworkManager.start();

        //Lancement d'UDP pour recevoir et traiter les paquets UDP (broadcast et unicast)
        UDPThread udpThread = new UDPThread();
        udpThread.start();

        //Création de l'User Manager au lancement de l'application
        UserManager userManager = udpThread.getUserManager();
        Input scanner = userManager.getScanner(); //Scanner pour pseudo et autres input plus bas

        //Saisie du pseudo pour la première connexion à l'application
        System.out.println("Saisissez votre pseudo : ");
        String firstPseudo = scanner.getNextLine();
        userManager.sendUDPConnexion(firstPseudo);

        //Boucle infinie pour les différentes fonctionnalités de base (changement de pseudo, initiation de conversation, déconnexion)
        while (true) {
            System.out.println("Pour vous deconnecter taper d, pour changer de pseudo taper m, pour initier une conversation taper s");
            String userPrompt = "";
            if(x == 0){ //Condition pour stopper le read de CE scanner lorsqu'une connexion TCP est lancée (utile seulement en ligne de commande)
                userPrompt = scanner.getNextLine(); //Lecture de l'entrée utilisateur;
            }
            if (userPrompt.equals("d")) {
                userManager.sendUDPDeconnexion();
            } else if (userPrompt.equals("m")) {
                userManager.sendUDPChangePseudo();
            } else if(userPrompt.equals("s")){
                userManager.sendTCP();
                x = 1;
            }
        }
    }
}


package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UserManager {

    private UDPThread udpThread;
    public UserManager () throws IOException {

        UDPThread udpThread = new UDPThread(); //Création du thread UDP qui permettra de recevoir les différentes notifs UDP

        //Début notifications broadcast pour la connection
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur

        String pseudo = myObj.nextLine();  //Lecture de l'entrée utilisateur;

        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
        String message = "connexion " +pseudo; //Création du payload du paquet UDP
        InetAddress broadcast = InetAddress.getByName("10.1.5.224"); //Adresse destination !!Doit etre un broadcast !!
        int port = 4445; //Port de destination du broadcast
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi de la notification de connexion
        dgramSocket.close(); //Fermeture du socket
        //Fin notifications broadcast pour la connection



        udpThread.start(); //Démarrage du thread UDP pour la reception des notifs UDP

    }

    public UDPThread getUdpThread() {
        return udpThread;
    }
}

package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;  // Import the Scanner class
import java.util.Timer;



/**
 * Hello world!
 *
 */
public class App
{

    public static void main( String[] args ) throws IOException, InterruptedException {

        UDPThread udpThread = new UDPThread(); //Création de l'User Manager au lancement de l'application
        udpThread.start();
        UserManager userManager = udpThread.getUserManager();

        userManager.sendUDP("c");
        Input scanner = userManager.getScanner();
        /*System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur

        String pseudo = myObj.nextLine();  //Lecture de l'entrée utilisateur;
        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
        String message = "c/" +pseudo; //Création du payload du paquet UDP
        InetAddress broadcast = InetAddress.getByName("127.0.0.1"); //Adresse destination !!Doit etre un broadcast !!
        int port = 4445; //Port de destination du broadcast
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi de la notification de connexion
        dgramSocket.close(); */

        while(true) {
            if(userManager.getGo()) {
                System.out.println("Pour vous deconnecter taper d, pour changer de pseudo taper m");
                String userPrompt = scanner.getNextLine();  //Lecture de l'entrée utilisateur;
                if (userPrompt.equals("d")) {
                    userManager.sendUDP("d");
                } else if (userPrompt.equals("m")) {
                    userManager.sendUDP("m");
                } else if (userPrompt.equals("gabu")) {
                    System.out.println("C'est moi qui l'ai eu");
                }
            } else {
                continue;
            }
        }
    }




}


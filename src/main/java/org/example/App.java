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

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur

        String pseudo = myObj.nextLine();  //Lecture de l'entrée utilisateur;
        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
        String message = "c/" +pseudo; //Création du payload du paquet UDP
        InetAddress broadcast = InetAddress.getByName("127.0.0.1"); //Adresse destination !!Doit etre un broadcast !!
        int port = 4445; //Port de destination du broadcast
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi de la notification de connexion
        dgramSocket.close();

        while(true) {
            Scanner myObj2 = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Pour vous deconnecter taper d");

            String value = myObj.nextLine();  //Lecture de l'entrée utilisateur;
            if (value.equals("d")) {
                DatagramSocket dgramSocket2 = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
                String message2 = "d/" + pseudo; //Création du payload du paquet UDP
                InetAddress broadcast2 = InetAddress.getByName("127.0.0.1"); //Adresse destination !!Doit etre un broadcast !!
                int port2 = 4445; //Port de destination du broadcast
                DatagramPacket outPacket2 = new DatagramPacket(message2.getBytes(), message2.length(), broadcast2, port2); //Création du datagramme UDP
                dgramSocket2.send(outPacket2); //Envoi de la notification de connexion
                dgramSocket2.close();
            }
        }
    }




}


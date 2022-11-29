package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;  // Import the Scanner class
import java.util.Timer;

import static org.example.UserManager.sendUDP;

/**
 * Hello world!
 *
 */
public class App
{

    public static void main( String[] args ) throws IOException, InterruptedException {

        UDPThread udpThread = new UDPThread(); //Création de l'User Manager au lancement de l'application
        udpThread.start();

        sendUDP("c");
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
            System.out.println("Pour vous deconnecter taper d, pour changer de pseudo taper m");
            Scanner prompt = new Scanner(System.in);
            String userPrompt = prompt.nextLine();  //Lecture de l'entrée utilisateur;
            if(userPrompt.equals("d"))
            {
                sendUDP("d");
            } else if(userPrompt.equals("m"))
            {
                sendUDP("m");
            }

        }
    }




}


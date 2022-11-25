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


    }




}


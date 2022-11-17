package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;  // Import the Scanner class

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws IOException {
        UDPListener recep = new UDPListener();
        recep.start();

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter pseudo : ");

        String pseudo = myObj.nextLine();  // Read user input

        System.out.println( "Hello World!" );
        DatagramSocket dgramSocket = new DatagramSocket();
        String message = "connexion " +pseudo;
        InetAddress broadcast = InetAddress.getByName("10.1.5.224");
        int port = 4445;
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port);
        dgramSocket.send(outPacket);




    }
}

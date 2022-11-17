package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws IOException {

        System.out.println( "Hello World!" );
        DatagramSocket dgramSocket = new DatagramSocket();
        String message = "connexion";
        InetAddress broadcast = InetAddress.getByName("10.1.5.224");
        int port = 4445;
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port);
        dgramSocket.send(outPacket);


        UDPListener recep = new UDPListener();
        recep.run();

    }
}

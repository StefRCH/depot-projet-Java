package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPListener extends Thread{
    private DatagramSocket socket;
    private byte[] buf = new byte[256];

    public UDPListener() throws SocketException {

        socket = new DatagramSocket(4445);

    }

    public void run(){
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {

            socket.receive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            System.out.println(address+ " " + port);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(received);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

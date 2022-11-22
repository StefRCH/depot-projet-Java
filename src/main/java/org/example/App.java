package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;  // Import the Scanner class

/**
 * Hello world!
 *
 */
public class App 
{
    private UserManager userManager;

    public static void main( String[] args ) throws IOException {

        UserManager userManager = new UserManager(); //Création de l'User Manager au lancement de l'application

    }

    public UserManager getUserManager() {
        return userManager;
    }
}

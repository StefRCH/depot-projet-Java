package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class TransmitterThread extends Thread {

    private Socket service;

    public TransmitterThread(Socket service) throws IOException {
        this.service = service; //On mémorise le tuyau de communication
    }

    public void run() {
        boolean quit = false;
        PrintStream flux = null;
        try {
            flux = new PrintStream(service.getOutputStream(), true); //J'isole le flux de comm en sortie (serveur vers client)

            while (!quit) { //Tant que le client n'a pas demandé à quitter
                Scanner myObj = new Scanner(System.in);  // Create a Scanner object
                System.out.println("Enter your message :");
                String message = myObj.nextLine();  // Read user input
                flux.println(message);
                //System.out.println("Username is: " + userName);  // Output user input
            }
        } catch (IOException e) {
            e.printStackTrace();
            quit = true;
        }
    }
}

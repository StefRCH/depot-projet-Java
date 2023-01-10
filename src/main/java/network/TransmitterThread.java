package network;

import user.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TransmitterThread extends Thread {

    private Socket sock;
    private User emetteur;

    public TransmitterThread(Socket sock) throws IOException {
        this.sock = sock; //On mémorise le tuyau de communication
    }

    public void run() {
        boolean quit = false;
        try {
            OutputStream outputStream = sock.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true); //J'isole le flux de comm en sortie (serveur vers client)

            while (!quit) { //Tant que le client n'a pas demandé à quitter
                Scanner myObj = new Scanner(System.in);  // Create a Scanner object
                System.out.println("Enter your message :");
                String message = myObj.nextLine();  // Read user input
                writer.println(message);
            }

            sock.close();

        } catch (IOException e) {
            e.printStackTrace();
            quit = true;
        }
    }
}

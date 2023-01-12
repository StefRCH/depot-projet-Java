package network;

import user.User;
import user.UserManager;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TransmitterThread extends Thread {

    private Socket sock;
    private Input scanner;

    public TransmitterThread(Socket sock) throws IOException {
        this.sock = sock; //On mémorise le tuyau de communication
        this.scanner = Input.getInstance();
    }

    public void run() {
        boolean quit = false;
        try {
            /*
            OutputStream outputStream = sock.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true); //J'isole le flux de comm en sortie (serveur vers client)
            */
            PrintStream flux = new PrintStream(sock.getOutputStream(), true); //J'isole le flux de comm en sortie (celui que l'on envoie)

            while (!quit) { //Tant que le client n'a pas demandé à quitter
                System.out.println("Enter your message :");
                String message = scanner.getNextLine(); //Read user input
                if(message == "quit"){
                    sock.close();
                    quit = true;
                }
                else{
                    flux.println(message); //Envoi du message
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

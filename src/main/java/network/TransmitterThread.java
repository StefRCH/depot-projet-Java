package network;

import BDD.DataBaseJava;
import user.ConversationObserver;

import java.io.*;
import java.net.Socket;

/**
 * Cette classe est appelée par le serveur TCP ou par l'user manager lors de l'initiation d'une conversation.
 * C'est un thread qui correspond au canal qui nous permet de recevoir les données nous étant destinées.
 */

public class TransmitterThread extends Thread implements ConversationObserver {

    private Socket sock;
    private Input scanner;

    private boolean send;

    private String message;

    public TransmitterThread(Socket sock) throws IOException {
        this.sock = sock; //On mémorise le tuyau de communication
        this.send=false;
        this.message="";
    }

    public void run() {
        boolean quit = false;
        try {
            PrintStream flux = new PrintStream(sock.getOutputStream(), true); //J'isole le flux de comm en sortie (celui que l'on envoie)
            while (!quit) { //Tant que le client n'a pas demandé à quitter

                if(this.send) {

                    String message = this.message; //Read user input
                    if(message == "/quit"){
                        sock.close();
                        quit = true;
                    }
                    else{
                        System.out.println(sock.getInetAddress());

                        flux.println(message); //Envoi du message
                        DataBaseJava.insertCom(sock.getInetAddress().toString(),sock.getLocalAddress().toString(),message);
                        System.out.println("addresse IP qui émet :"+sock.getInetAddress().toString());
                    }
                    this.send=false;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFromConv(String action, String pseudo, Message message) {

        if (action.equals("sendMessage") && sock.getInetAddress().toString().equals(pseudo)) //On verifie que l ip est la meme, si oui on envoi
        {

            this.message=message.getPayload(); //On definit le message
            this.send=true; //On passe la variable a true pour la boucle

        }
    }
}

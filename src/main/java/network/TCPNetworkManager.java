package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPNetworkManager extends Thread{
    private IPv4 server;

    public void run(){
        //Création du socket serveur
        this.server = new IPv4();
        ServerSocket s = null;

        try {
            s = new ServerSocket (4000,2,this.server.getIPv4());
            System.out.println("Serveur de socket créé : " + s);
        }
        catch (IOException e)
        {
            System.err.println ("Erreur socket " + e) ;
            System.exit (1) ;
        }

        while(true){ //Sert à avoir plusieurs clients en même temps
            System.out.println("En attente de connexion...");
            try {
                Socket service = s.accept(); //Le serveur se met en écoute, dans l'attente d'une requête client
                System.out.println(service);
                System.out.println("Client connecté : " + s); //Service est le socket (tuyau de communication) vers le client qui vient de se connecter
                App.x = 1; // Pour empêcher le scanner de nous embêter durant l'échange

                //Création du thread permettant d'envoyer des messages
                TransmitterThread transmit = new TransmitterThread(service);
                transmit.start(); //On lance le Thread càd la méthode run() dans TransmitterThread

                //Création du thread permettant de recevoir des messages
                ReceiverThread receive = new ReceiverThread(service);
                receive.start(); //On lance le Thread càd la méthode run() dans ReceiverThread

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

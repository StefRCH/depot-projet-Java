package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPNetworkManager extends Thread{
    public void run(){
        ServerSocket s = null;

        try {
            s = new ServerSocket (4000) ;
            System.out.println("Serveur de socket créé : " + s);
        }
        catch (IOException e)
        {
            System.err.println ("Erreur socket " + e) ;
            System.exit (1) ;
        }

        while(true){ //Sert pour avoir plusieurs clients en même temps
            System.out.println("En attente de connexion...");
            Socket service = null;//On attend l'arrivée d'un client (méthode accept() bloquante)
            try {

                service = s.accept();
                System.out.println("Client connecté :" + s); //Service est le socket (tuyau de communication) vers le client qui vient de se connecter

                TransmitterThread transmit = null; //Pour chaque client connecté on crée un Thread qui va gérer les communications
                transmit = new TransmitterThread(service);
                transmit.start(); //On lance le Thread (--> run() dans TransmitterThread)

                ReceiverThread runnableReceive = null;
                Thread receive = new Thread(runnableReceive);
                receive.start(); //On lance le Thread (--> run() dans ReceiverThread)

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}

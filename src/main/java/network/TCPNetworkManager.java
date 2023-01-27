package network;

import network.*;
import user.ConversationObservable;
import user.ConversationObserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Cette classe correspond au serveur TCP qui accepte les  connexions entrantes
 * Elle permet de run les threads "transmitter" et "receiver" correspondant aux canaux ascendants et descendants de la connexion avec un autre user
 */
public class TCPNetworkManager extends Thread implements TCPObservable {
    private IPv4 server;

    private ArrayList observerList;

    public TCPNetworkManager() {
        this.observerList = new ArrayList<>();

    }

    public void run() {
        //Création du socket serveur
        this.server = new IPv4();
        ServerSocket s = null;

        try {
            //Assignation du port au socket du serveur
            s = new ServerSocket(4000);
            System.out.println("Serveur de socket créé : " + s);
        } catch (IOException e) {
            System.err.println("Erreur socket " + e);
            System.exit(1);
        }

        while (true) { //Sert à revenir en état d'écoute après avoir accepté un client
            System.out.println("En attente de connexion...");
            try {
                Socket service = s.accept(); //Le serveur se met en écoute, dans l'attente d'une requête client
                System.out.println("Client connecté : " + s); //Service est le socket (tuyau de communication) vers le client qui vient de se connecter
                App.x = 1; // Pour bloquer le scanner de la classe App durant l'échange

                //Création du thread permettant d'envoyer des messages
                TransmitterThread transmitRunnable = new TransmitterThread(service);


                //Création du thread permettant de recevoir des messages
                ReceiverThread receiveRunnable = new ReceiverThread(service);


                this.notifyObserverConv("newConv", receiveRunnable, transmitRunnable, service.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void addObserver(TCPObserver o) { //Permet d'ajouter un observer
        this.observerList.add(o);
    }

    @Override
    public void removeObserver(TCPObserver o) { //Permet d'enlever un observer
        this.observerList.remove(o);
    }

    @Override
    public void notifyObserver(String action, String ip, Message message) { //Permet de notfier les observers
        for (int i = 0; i < this.observerList.size(); i++) {
            TCPObserver o = (TCPObserver) this.observerList.get(i);
            o.updateFromTCP(action, ip, message);
        }
    }

    @Override
    public void notifyObserverConv(String action, ReceiverThread receiveRunnable, TransmitterThread transmitRunnable, InetAddress ip) { //Permet de notfier les observers
        for (int i = 0; i < this.observerList.size(); i++) {
            TCPObserver o = (TCPObserver) this.observerList.get(i);
            o.updateFromTCPManager(action, receiveRunnable, transmitRunnable, ip);
        }
    }
}

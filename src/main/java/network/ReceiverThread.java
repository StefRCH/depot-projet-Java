package network;

import BDD.DataBaseJava;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cette classe est appelée par le serveur TCP ou par l'user manager lors de l'initiation d'une conversation.
 * C'est un thread qui correspond au canal qui permet de faire transiter les données que l'on envoie.
 */
public class ReceiverThread extends Thread implements TCPObservable {

    private Socket sock;
    private ArrayList observerList;

    private String oldMessage;

    public ReceiverThread(Socket sock) {
        this.sock = sock;
        this.observerList = new ArrayList<>();
        this.oldMessage = "";
    }

    public void run() {

        boolean quit = false; //Variable pour la boucle while
        try {
            BufferedReader requete = new BufferedReader(new InputStreamReader(sock.getInputStream())); //J'isole le flux de comm en entrée (ce que l'on reçoit)

            while (!quit) { //Afin de toujours être en écoute d'un potentiel nouveau message
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                String newMessage = requete.readLine(); // Attente d'un message en entrée | ATTENTION --> readline() est bloquant
                if(newMessage == null || newMessage.equals(""))
                    continue;

                //Formatage du message afin d'afficher la date de réception de celui-ci
                this.oldMessage = newMessage;
                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                System.out.println("adresse IP qui a émi : "+sock.getInetAddress().toString());
                Message received = new Message(newMessage, date);
                //On envoie au conversation manager, le nouveau message et l'IP de la personne
                System.out.println(received.getPayload());
                this.notifyObserver("newMessage", sock.getInetAddress().toString().substring(1), received);
                DataBaseJava.insertCom(sock.getLocalAddress().toString(),sock.getInetAddress().toString(),newMessage);
            }
        } catch(IOException e){
            e.printStackTrace();
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
    public void notifyObserverConv(String action, ReceiverThread receiveRunnable, TransmitterThread transmitRunnable, InetAddress ip) {

    }
}
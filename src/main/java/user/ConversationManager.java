package user;

import network.Message;
import network.ReceiverThread;
import network.TCPObserver;
import network.TransmitterThread;
import userInterface.GraphicObserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe correspond à celle gérant le multi-threading de l'application
 * ELle gère les différentes conversations avec les différents utilisateurs
 */

public class ConversationManager implements ConversationObservable, GraphicObserver, TCPObserver {

    private ArrayList<User> users;
    private int nbThread;
    private ArrayList<Conversation> listeConversations;

    private ArrayList observerList;


    public ConversationManager(ArrayList<User> users){

        this.listeConversations = new ArrayList<>(); //Creation liste conversation
        this.users = users; //Recupération liste users
        this.observerList = new ArrayList();// Creation de la liste d'observer

    }

    // Méthode permettant de créer un thread pour la conversation avec un autre utilisateur
    public Conversation createNewThread(User user){
        return null;
    }

    // Methode permettant de créer un message à envoyer à un utilisateur (via threadTransmitter)
    public int sendMessage(User user){
        return 0;
    }

    // Méthode permettant de recevoir des messages (via threadListener)
    public int listenMessage(){
        return 0;
    }

    // Méthode vérifiant si un Thread existe déjà
    public Conversation isThreadAlreadyExisting(User user, String message){
        return null;
    }

    public void messageReceived(String ip, Message message) {
        for(User user : users)
        {
            if(user.getIpAddress().toString().substring(1).equals(ip)) //On cherche la correspondance des ip avec les users connectés pour savoir de qui vient le message
            {
                this.notifyObserver("newMessage", user.getPseudo(), message);
            }

        }

    }

    public boolean sendTCP(String userName) throws IOException {
        //Démarrage d'une conversation
        String userIP; //Variable pour stocker IP de l'utilisateur avec qui l'on souhaite échanger
        int port = 4000; //numéro de port du serveur
        for(User n : users){
            if (n.getPseudo().equals(userName)){
                userIP = n.getIpAddress().toString().substring(1); //On récupère l'adresse de l'utilisateur avec lequel on souhaite échanger (adresse que l'on va utiliser pour se connecter au serveur TCP)
                Socket socket = new Socket(userIP, port); //Création du socket avec comme paramètres les variables créées ci-dessus

                // On lance les threads d'échange afin d'envoyer et recevoir des messages
                TransmitterThread transmit = new TransmitterThread(socket);
                transmit.start(); //On lance le Thread (--> run() dans TransmitterThread)

                ReceiverThread receive = new ReceiverThread(socket);
                receive.start(); //On lance le Thread (--> run() dans ReceiverThread
                receive.addObserver(this);

                System.out.println("SUCCESS ---- Connexion établie");
                return true;
            }
            System.out.println("ERROR ---- Impossible d'établir une connexion avec cet utilisateur");
        }
        return false;
    }

    @Override
    public void addObserver(ConversationObserver o) {
        this.observerList.add(o);
    }

    @Override
    public void removeObserver(ConversationObserver o) {
        this.observerList.remove(o);
    }

    @Override
    public void notifyObserver(String action, String pseudo, Message message) {
        for (int i = 0; i < this.observerList.size(); i++) {
            ConversationObserver o = (ConversationObserver) observerList.get(i);
            o.updateFromConv(action, pseudo, message);
        }
    }

    @Override
    public void updateFromGUI(String action, String pseudo) throws IOException {
        if (action.equals("initiateConv")) {
            this.sendTCP(pseudo);
        }
    }

    @Override
    public void updateFromTCP(String action, String ip, Message message) {
        if(action.equals("newMessage")) {
            this.messageReceived(ip, message);
        }
    }
}

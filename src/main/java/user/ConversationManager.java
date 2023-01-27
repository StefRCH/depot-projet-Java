package user;

import network.Message;
import network.ReceiverThread;
import network.TCPObserver;
import network.TransmitterThread;
import userInterface.GraphicObserver;

import java.io.IOException;
import java.net.InetAddress;
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
    private ArrayList<Conversation> conversationList;

    private ArrayList observerList;


    public ConversationManager(ArrayList<User> users){

        this.conversationList = new ArrayList<>(); //Creation liste conversation
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


        for(Conversation conv : conversationList) { //On parcours les conversations pour retrouver laquelle est la bonne
            System.out.println(conv.getUser().getPseudo());
            if(conv.getUser().getIpAddress().toString().substring(1).equals(ip)) {
                ArrayList listDesMessages = conv.getMessageList();
                if(listDesMessages.size() != 0) {
                    Message denierMessage = (Message) listDesMessages.get(listDesMessages.size() - 1);
                    if(denierMessage.getPayload().equals(message.getPayload()) && denierMessage.getDate().equals(message.getDate())) //Evite de recevoir deux fois le meme message
                        break;
                }



                conv.addMessage(message); //On ajoute le message a la conversation
                this.notifyObserver("newMessage", conv.getUser().getPseudo(), message);
            }
        }

    }
    public void sendMessage(String pseudo, Message message) {
        System.out.println(pseudo);
        for(Conversation conv : conversationList) { //On parcours les conversations pour retrouver laquelle est la bonne
            System.out.println(conv.getUser().getPseudo());
            if(conv.getUser().getPseudo().equals(pseudo)) {
                conv.addMessage(message); //On ajoute le message a la conversation
                this.notifyObserver("sendMessage",conv.getUser().getIpAddress().toString(),message); //On notifie le thread emetteur, on lui envoi l ip et non pas le pseudo car il a l'IP
            }
        }

    }
    private void deconnexionUser(String pseudo) {
        for(Conversation conv : conversationList) {
            if (conv.getUser().getPseudo().equals(pseudo)) {
                try {
                    conv.getTransmitterThread().interrupt();
                    conv.getReceiverThread().interrupt();
                } catch (Exception e) {
                    System.out.println("Intteruption thread");
                }
            }
        }
        this.conversationList.removeIf(user -> user.getUser().getPseudo().equals(pseudo));
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
                TransmitterThread transmitRunnable = new TransmitterThread(socket);
                this.addObserver(transmitRunnable);
                Thread transmit = new Thread(transmitRunnable);
                transmit.start(); //On lance le Thread (--> run() dans TransmitterThread)


                ReceiverThread receiveRunnable = new ReceiverThread(socket);
                receiveRunnable.addObserver(this);
                Thread receive = new Thread(receiveRunnable);
                receive.start(); //On lance le Thread (--> run() dans ReceiverThread


                Conversation conv = new Conversation(receive, transmit, n);
                this.conversationList.add(conv);

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
    public void updateFromGUI(String action, String pseudo, Message message) throws IOException {
        if (action.equals("initiateConv")) {
            this.sendTCP(pseudo);
        } else if (action.equals("sendMessage")) {
            System.out.println("dans le updateFromGui");
            this.sendMessage(pseudo, message);
        } else if (action.equals("deconnexionConv")) { //Si l'observable (MainSceneController) notify avec deconnexion, alors j'envoie un udp de deconnexion
            /*for (Conversation conversation : conversationList) {
                try {
                    conversation.getTransmitterThread().interrupt();
                    conversation.getReceiverThread().interrupt();
                } catch (Exception e) {
                    System.out.println("Intteruption thread");
                }
            }*/
        } else if (action.equals("deconnexionUser")) {
            this.deconnexionUser(pseudo);
        }
    }



    @Override
    public void updateFromTCP(String action, String ip, Message message) {
        if(action.equals("newMessage")) {
            System.out.println("je suis dans updatefromTCP");

            this.messageReceived(ip, message);
        }
    }

    @Override
    synchronized public void updateFromTCPManager(String action, ReceiverThread receiveRunnable, TransmitterThread transmitRunnable, InetAddress ip) {
        for (User n : users) {
            if (n.getIpAddress().equals(ip)) {
                this.addObserver(transmitRunnable);
                Thread transmit = new Thread(transmitRunnable);
                transmit.start(); //On lance le Thread (--> run() dans TransmitterThread)

                receiveRunnable.addObserver(this);
                Thread receive = new Thread(receiveRunnable);
                receive.start(); //On lance le Thread (--> run() dans ReceiverThread



                Conversation conv = new Conversation(receive, transmit, n);
                this.conversationList.add(conv);

                this.notifyObserver("newConv", n.getPseudo(), null );
            }
        }

    }

}

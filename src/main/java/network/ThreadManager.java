package network;

import user.Conversation;
import user.User;

import java.util.ArrayList;

/**
 * Cette classe correspond à celle gérant le multi-threading de l'application
 * ELle gère les différentes conversations avec les différents utilisateurs
 */

public class ThreadManager {
    private int nbThread;
    private ArrayList<Conversation> listeConversations;

    public ThreadManager(){
        listeConversations = new ArrayList<>();
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
}

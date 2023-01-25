package user;

import network.Message;

public interface ConversationObservable {
    // Méthode permettant d'ajouter (abonner) un observateur.
    public void addObserver(ConversationObserver o);
    // Méthode permettant de supprimer (résilier) un observateur.
    public void removeObserver(ConversationObserver o);
    // Méthode qui permet d'avertir tous les observateurs lors d'un changement d'état.
    public void notifyObserver(String action, String pseudo, Message message);

}

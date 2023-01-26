package userInterface;

import network.Message;
import user.UserObserver;

public interface GraphicObservable {



    // Méthode permettant d'ajouter (abonner) un observateur.
    public void addObserver(GraphicObserver o);
    // Méthode permettant de supprimer (résilier) un observateur.
    public void removeObserver(GraphicObserver o);
    // Méthode qui permet d'avertir tous les observateurs lors d'un changement d'état.
    public void notifyObserver(String action, String pseudo, Message message);




}

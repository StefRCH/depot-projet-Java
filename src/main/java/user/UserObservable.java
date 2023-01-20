package user;

import java.util.Observer;

public interface UserObservable {


    // Méthode permettant d'ajouter (abonner) un observateur.
    public void addObserver(UserObserver o);
    // Méthode permettant de supprimer (résilier) un observateur.
    public void removeObserver(UserObserver o);
    // Méthode qui permet d'avertir tous les observateurs lors d'un changement d'état.
    public void notifyObserver(String action, String pseudo);


}

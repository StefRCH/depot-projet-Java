package network;

import java.util.List;

public interface UDPObservable {

    // Méthode permettant d'ajouter (abonner) un observateur.
    public void addObserver(UDPObserver o);
    // Méthode permettant de supprimer (résilier) un observateur.
    public void removeObserver(UDPObserver o);
    // Méthode qui permet d'avertir tous les observateurs lors d'un changement d'état.
    public void notifyObserver(String action, List<String> data);

}

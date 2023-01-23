package network;

import userInterface.GraphicObserver;

import java.util.List;

public interface UDPObservabe {

    // Méthode permettant d'ajouter (abonner) un observateur.
    public void addObserver(UDPObserver o);
    // Méthode permettant de supprimer (résilier) un observateur.
    public void removeObserver(UDPObserver o);
    // Méthode qui permet d'avertir tous les observateurs lors d'un changement d'état.
    public void notifyObserver(String action, List<String> data);

}

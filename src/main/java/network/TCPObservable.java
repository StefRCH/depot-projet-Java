package network;

import java.net.InetAddress;
import java.util.List;

public interface TCPObservable {

    // Méthode permettant d'ajouter (abonner) un observateur.
    public void addObserver(TCPObserver o);
    // Méthode permettant de supprimer (résilier) un observateur.
    public void removeObserver(TCPObserver o);
    // Méthode qui permet d'avertir tous les observateurs lors d'un changement d'état.
    public void notifyObserver(String action, String ip, Message message);

    public void notifyObserverConv(String action, Thread receiver, Thread transmit, InetAddress ip);


}

package network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe correspond à celle gérant la réception et le traitement des datagrammes
 */

public class UDPThread extends Thread implements UDPObservable {

    private ArrayList observerList;
    private DatagramSocket socket; //Création du socket de réception
    private byte[] buf = new byte[256]; //Buffer permettant de récuperer le payload de l'UDP
    private boolean close = false; //Permet de faire une boucle infinie

    private List<String> dataList; //Liste qui stockera l'ensemble des connexion/deconnexion/changement de pseudo

    private IPv4 host;

    public UDPThread() throws IOException {
        this.host = new IPv4();
        socket = new DatagramSocket(4445); //Création du socket sur le port 4445
        this.dataList = new ArrayList<String> (); //Création de la liste pour receptionner les datas
        this.observerList = new ArrayList<>();
    }

    public void run(){
        try {
            while(!close) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length); //Création d'un paquet vide
                socket.receive(packet); //En attente de récuperer un paquet (bloquant)
                InetAddress address = packet.getAddress(); //Recuperation de l'addresse IP source du paquet UDP
                String received = new String(packet.getData(), 0, packet.getLength()); // Conversion des datas en string
                received += address.toString();
                if(address.equals(this.host.getIPv4())) //Permet de verifier que le paquet recu est pas celui que nous avons nous même émis en broadcast
                {
                    continue;
                }

                this.dataList.add(received); //Ajout du payload UDP dans la liste
                System.out.println("data recue : " + this.dataList.get(0));


                this.notifyObserver("newData", this.dataList); //Notification a l'observer de la reception de nouvelles données
                this.dataList.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getUdpData()
    {
        return this.dataList; //On renvoie la liste avec les données
    }

    public void clearData() {this.dataList.clear();} //Remise à 0 des données après les avoir récupérées

    @Override
    public void addObserver(UDPObserver o) { //Permet d'ajouter un observer
        this.observerList.add(o);
    }

    @Override
    public void removeObserver(UDPObserver o) { //Permet d'enlever un observer
        this.observerList.remove(o);
    }

    @Override
    public void notifyObserver(String action, List<String> data) { //Permet de notfier les observers
        for (int i = 0; i < this.observerList.size(); i++) {
            UDPObserver o = (UDPObserver) this.observerList.get(i);
            o.updateFromUDP(action, data);
        }
    }
}
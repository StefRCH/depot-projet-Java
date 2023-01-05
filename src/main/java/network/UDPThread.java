package network;

import user.UserManager;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class UDPThread extends Thread {
    private DatagramSocket socket; //Création du socket de réception
    private byte[] buf = new byte[256]; //Buffer permettant de récuperer le payload de l'UDP
    private boolean close = false; //Permet de faire une boucle infinie

    private List<String> dataList; //Liste qui stockera l'ensemble des connexion/deconnexion/changement de pseudo

    private UserManager userManager; // Crée un lien entre UDPThread et UserManager

    private InetAddress notreIP;

    public UDPThread() throws IOException {

        this.userManager = new UserManager();
        socket = new DatagramSocket(4445); //Création du socket sur le port 4445
        this.dataList = new ArrayList<String> (); //Création de la liste pour receptionner les datas

    }

    public void run(){

        try {
            // Ce code permet de récuperer notre IPv4. Le simple getHostAddress ne fonctionne pas sur les PC de l'INSA
            try {
                Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
                while( networkInterfaceEnumeration.hasMoreElements()){
                    for ( InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses())
                        if ( interfaceAddress.getAddress().isSiteLocalAddress()) {
                            this.notreIP = InetAddress.getByName(interfaceAddress.getAddress().getHostAddress());
                            System.out.println(this.notreIP);

                        }

                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while(!close) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length); //Création d'un paquet vide
                socket.receive(packet); //En attente de récuperer un paquet (bloquant)
                InetAddress address = packet.getAddress(); //Recuperation de l'addresse IP source du paquet UDP
                String received = new String(packet.getData(), 0, packet.getLength()); // Conversion des datas en string
                received += address.toString();
                if(address.equals(this.notreIP)) //Permet de verifier que le paquet recu est pas celui que nous avons nous même émis en broadcast
                {
                    continue;
                }

                this.dataList.add(received); //Ajout du payload UDP dans la liste
                System.out.println(this.dataList.get(0));

                this.userManager.update(this.dataList);
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

    public UserManager getUserManager() { return  this.userManager; }

}
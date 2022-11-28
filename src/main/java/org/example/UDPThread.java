package org.example;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UDPThread extends Thread {
    private DatagramSocket socket; //Création du socket de réception
    private byte[] buf = new byte[256]; //Buffer permettant de récuperer le payload de l'UDP
    private boolean close = false; //Permet de faire une boucle infinie

    private List<String> dataList; //List qui stockera l'ensemble des connexion/deconnexion/changement de pseudo

    private UserManager userManager;

    public UDPThread() throws IOException {

        this.userManager = new UserManager();
        socket = new DatagramSocket(4445); //Création du socket sur le port 4445
        this.dataList = new ArrayList<String> (); //Création de la list pour receptionner les datas


    }

    public void run(){

        try {
            while(!close) {

                DatagramPacket packet = new DatagramPacket(buf, buf.length); //Création d'un paquet vide
                socket.receive(packet); //En attente de récuperer un paquet, bloquant
                InetAddress address = packet.getAddress(); //Recuperation de l'addresse IP source du paquet UDP
                String received = new String(packet.getData(), 0, packet.getLength()); //Convertission des datas en string
                received += address.toString();
                this.dataList.add(received); //Ajout du payload UDP dans la list
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
        return this.dataList; //On renvoi la liste avec les données

    }

    public void clearData()
    {
        this.dataList.clear(); //Remise à 0 des données après les avoir récupérée
    }

}
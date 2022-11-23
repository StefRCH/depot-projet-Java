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

    public UDPThread() throws IOException {


        socket = new DatagramSocket(4445); //Création du socket sur le port 4445
        this.dataList = new ArrayList<String>(); //Création de la list pour receptionner les datas


    }

    public void run() {

        try {
            while (!close) {

                DatagramPacket packet = new DatagramPacket(buf, buf.length); //Création d'un paquet vide
                socket.receive(packet); //En attente de récuperer un paquet (bloquant)
                InetAddress address = packet.getAddress(); //Recuperation de l'adresse IP source du paquet UDP
                System.out.println(address);
                String received = new String(packet.getData(), 0, packet.getLength()); //Convertission des datas en string
                System.out.println(received);
                this.dataList.add(received); //Ajout du payload UDP dans la list
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getUdpData() {
        return this.dataList; //On renvoie la liste avec les données

    }

    public void clearData() {
        this.dataList.clear(); //Remise à 0 des données après les avoir récupérée
    }

}



package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

public class UserManager {

    private ArrayList<User> users = new ArrayList<>();

    public UserManager () throws IOException {

        //Début notifications broadcast pour la connection
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur

        String pseudo = myObj.nextLine();  //Lecture de l'entrée utilisateur;
        System.out.println("hello");
        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
        String message = "c/" +pseudo; //Création du payload du paquet UDP
        InetAddress broadcast = InetAddress.getByName("127.0.0.1"); //Adresse destination !!Doit etre un broadcast !!
        int port = 4445; //Port de destination du broadcast
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi de la notification de connexion
        dgramSocket.close(); //Fermeture du socket
        //Fin notifications broadcast pour la connection


    }


    public void update(List<String> newData) {

        //this.udpThread.clearData(); //Remise à 0 de la liste.
        for(int i = 0; i < newData.size() ; i++)
        {
            System.out.println("hello");
            String data[] = newData.get(i).split("/");
            for(int y = 0 ; y<data.length; y++)
                System.out.println(data[y]);
            /*if(data[0] == "c")
            {

            } else if () {

            }*/


        }



    }

    // Méthode permettant d'ajouter un utilisateur à la liste "users" et qui return un message confirmant l'ajout de cet utilisateur
    public String createUser(User newUser) {
        users.add(newUser); // Ajout du nouvel utilisateur créé à la liste des utilisateurs


        // Boucle pour parcourir la liste et vérifier que l'utilisateur a bien été ajouté
        for(User n : users)
            if (n.getPseudo() == newUser.getPseudo()){
                return "SUCCESS ---- User : " + newUser.getPseudo() + " with @IP = " + newUser.getIpAddress() + "has SUCCESSFULLY been added to the list of users";
        }

        return "ERROR ---- User : " + newUser.getPseudo() + " with @IP = " + newUser.getIpAddress() + "has NOT been added to the list of users";
    }

    // Méthode permettant de supprimer un utilisateur de la liste "users" et return un message confirmant la suppression
    public String deleteUser(User user){
        users.remove(user);
        for(User n : users)
            if (n.getPseudo() == user.getPseudo()){
                return "ERROR ---- User : " + user.getPseudo() + " with @IP = " + user.getIpAddress() + "has NOT been deleted from the list of users";
            }
        return "SUCCESS ---- User : " + user.getPseudo() + " with @IP = " + user.getIpAddress() + " has been deleted from the list of users";
    }
}

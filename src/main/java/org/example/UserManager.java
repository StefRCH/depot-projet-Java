package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

public class UserManager {

    private ArrayList<User> users = new ArrayList<>();

    public UserManager () throws IOException {

        //Début notifications broadcast pour la connection
        /*Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur

        String pseudo = myObj.nextLine();  //Lecture de l'entrée utilisateur;
        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
        String message = "c/" +pseudo; //Création du payload du paquet UDP
        InetAddress broadcast = InetAddress.getByName("127.0.0.1"); //Adresse destination !!Doit etre un broadcast !!
        int port = 4445; //Port de destination du broadcast
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi de la notification de connexion
        dgramSocket.close(); //Fermeture du socket */
        //Fin notifications broadcast pour la connection


    }


    public void update(List<String> dataList) throws UnknownHostException {

        List<String> newData =  dataList; //IL FAUT CLONER LA LISTE ET PAS LA PASSER EN REFERENCE !!!!!!!!
        //this.udpThread.clearData(); //Remise à 0 de la liste.
        for(int i = 0; i < newData.size() ; i++)
        {
            String data[] = newData.get(i).split("/");


            InetAddress ipAddress = InetAddress.getByName(data[2]); //Conversion de l'addresse ip recu de String en InetAddress


            if(data[0].equals("c")) //C'est une connexion
            {
                System.out.println(this.createUser(data[1], ipAddress));

            } else if (data[0].equals("d")) { //C'est une déconnexion
                System.out.println(this.deleteUser(data[1], ipAddress));
            } else { //C'est unn changement de pseudo

            }



        }
        for(User n : users)
        {
            System.out.println(n.getPseudo());
        }




    }

    // Méthode permettant d'ajouter un utilisateur à la liste "users" et qui return un message confirmant l'ajout de cet utilisateur
    public String createUser(String pseudo, InetAddress ipAddress) {



        //Conversion de l'addresse IP en InetAddress
        User newUser = new User(pseudo, ipAddress); //Création d'un new user

        this.users.add(newUser); //Ajout de l'user a la liste

        // Boucle pour parcourir la liste et vérifier que l'utilisateur a bien été ajouté
        for(User n : this.users)
            if (n.getPseudo().equals(newUser.getPseudo())){
                return "SUCCESS ---- User : " + newUser.getPseudo() + " with @IP = " + newUser.getIpAddress() + " has SUCCESSFULLY been added to the list of users";
        }

        return "ERROR ---- User : " + newUser.getPseudo() + " with @IP = " + newUser.getIpAddress() + " has NOT been added to the list of users";
    }

    // Méthode permettant de supprimer un utilisateur de la liste "users" et return un message confirmant la suppression
    public String deleteUser(String pseudo, InetAddress ipAddress) {

        for(User n : users) //On cherche dans la liste pour retrouver l'user et le supprimer
        {
            if(n.getPseudo().equals(pseudo) && n.getIpAddress().equals(ipAddress));
                users.remove(n);
        }

        for(User n : this.users)
            if (n.getPseudo().equals(pseudo)){
                return "ERROR ---- User : " + pseudo + " with @IP = " + ipAddress + " has NOT been deleted from the list of users";
            }
        return "SUCCESS ---- User : " + pseudo + " with @IP = " + ipAddress + " has been deleted from the list of users";
    }
}

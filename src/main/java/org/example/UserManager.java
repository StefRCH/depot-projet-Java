package org.example;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.function.Predicate;

public class UserManager {

    public ArrayList<User> users = new ArrayList<>();

    public UserManager () throws IOException {

        //Début notifications broadcast pour la connection

        //Fin notifications broadcast pour la connection


    }


    public void update(List<String> dataList) throws IOException {

        List<String> newData =  new ArrayList<String>(dataList);
        //this.udpThread.clearData(); //Remise à 0 de la liste.
        for(int i = 0; i < newData.size() ; i++)
        {
            String data[] = newData.get(i).split("/");


            InetAddress ipAddress = InetAddress.getByName(data[2]); //Conversion de l'addresse ip recu de String en InetAddress


            if(data[0].equals("c")) //C'est une connexion
            {
                System.out.println(this.createUser(data[1], ipAddress));
                System.out.println(data[1]);

            } else if (data[0].equals("d")) { //C'est une déconnexion
                System.out.println(this.deleteUser(data[1], ipAddress));
            } else if (data[0].equals("m")) { //C'est unn changement de pseudo
                for(User n : users) //On verifie que le pseudo n'est pas déjà utilisé
                    if (n.getPseudo().equals(data[1])){
                        System.out.println("ERROR ---- The pseudo is already used.");
                        String message =data[1] + data[2];
                        sendUDP("w");
                    }

                for(User n : users) //On verifie que le pseudo n'est pas déjà utilisé
                    if (n.getIpAddress().equals(data[2])){
                        n.setPseudo(data[1]);
                        System.out.println("SUCCESS ---- The pseudo has been changed");
                    }

            } else if (data[0].equals("w")) {
                System.out.println("ERROR ---- Please choose another Pseudo, this one is already used");
                sendUDP("m");
            }



        }





    }



    // Méthode permettant d'ajouter un utilisateur à la liste "users" et qui return un message confirmant l'ajout de cet utilisateur
    public String createUser(String pseudo, InetAddress ipAddress) throws IOException {

        for(User n : users)
            if (n.getPseudo().equals(pseudo)){
                String message = pseudo +ipAddress;
                sendUDP("w");
                return "ERROR ---- The pseudo is already used.";

            }

        //Conversion de l'addresse IP en InetAddress
        User newUser = new User(pseudo, ipAddress); //Création d'un new user

        users.add(newUser); //Ajout de l'user a la liste

        // Boucle pour parcourir la liste et vérifier que l'utilisateur a bien été ajouté
        for(User n : users)
            if (n.getPseudo().equals(newUser.getPseudo())){
                return "SUCCESS ---- User : " + newUser.getPseudo() + " with @IP = " + newUser.getIpAddress() + " has SUCCESSFULLY been added to the list of users";
        }

        return "ERROR ---- User : " + newUser.getPseudo() + " with @IP = " + newUser.getIpAddress() + " has NOT been added to the list of users";
    }

    // Méthode permettant de supprimer un utilisateur de la liste "users" et return un message confirmant la suppression
    public String deleteUser(String pseudo, InetAddress ipAddress) {

        users.removeIf(user -> user.getPseudo().equals(pseudo) &&  user.getIpAddress().equals(ipAddress));

        for(User n : users)
            if (n.getPseudo().equals(pseudo)){
                return "ERROR ---- User : " + pseudo + " with @IP = " + ipAddress + " has NOT been deleted from the list of users";
            }
        return "SUCCESS ---- User : " + pseudo + " with @IP = " + ipAddress + " has been deleted from the list of users";
    }

    public String sendUDP(String type) throws IOException {
        Scanner myObj = new Scanner(System.in);
        if(type.equals("c")) {

            // Create a Scanner object
            System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur
            String pseudo = myObj.nextLine();  //Lecture de l'entrée utilisateur;
            this.createUser(pseudo, InetAddress.getByName("127.0.0.1"));
            DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
            String message = "c/" + pseudo; //Création du payload du paquet UDP
            InetAddress broadcast = InetAddress.getByName("10.1.255.255"); //Adresse destination !!Doit etre un broadcast !!
            int port = 4445; //Port de destination du broadcast
            DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), broadcast, port); //Création du datagramme UDP
            dgramSocket.send(outPacket); //Envoi de la notification de connexion
            dgramSocket.close(); //Fermeture du socket
            return "Connection successful";
        } else if (type.equals("d")) { //Envoi d'un broadcast de deconnexion
            DatagramSocket dgramSocket2 = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
            String message2 = "d/" + users.get(0).getPseudo();
            System.out.println(message2.toString());//Création du payload du paquet UDP
            InetAddress broadcast2 = InetAddress.getByName("10.1.255.255"); //Adresse destination !!Doit etre un broadcast !!
            int port2 = 4445; //Port de destination du broadcast
            DatagramPacket outPacket2 = new DatagramPacket(message2.getBytes(), message2.length(), broadcast2, port2); //Création du datagramme UDP
            dgramSocket2.send(outPacket2); //Envoi de la notification de connexion
            dgramSocket2.close();
            return "logout successful";
        } else if (type.equals("m")) { //Envoi d'un changement de pseudo
            System.out.println("Rentrez votre nouveau pseudo : ");
            String newPseudo = myObj.nextLine();
            DatagramSocket dgramSocket2 = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
            String message2 = "m/" + newPseudo; //Création du payload du paquet UDP
            InetAddress broadcast2 = InetAddress.getByName("10.1.255.255"); //Adresse destination !!Doit etre un broadcast !!
            int port2 = 4445; //Port de destination du broadcast
            DatagramPacket outPacket2 = new DatagramPacket(message2.getBytes(), message2.length(), broadcast2, port2); //Création du datagramme UDP
            dgramSocket2.send(outPacket2); //Envoi de la notification de connexion
            dgramSocket2.close();
            return "Changing pseudo successful";
        } else if(type.equals("w")) { //Wrong pseudo, on envoi ce paquet si la personne a prit un pseudo deja utilisé.

            DatagramSocket dgramSocket2 = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
            String message2 = "w/"; //Création du payload du paquet UDP
            InetAddress broadcast2 = InetAddress.getByName("10.1.255.255"); //Adresse destination !!Doit etre un broadcast !!
            int port2 = 4445; //Port de destination du broadcast
            DatagramPacket outPacket2 = new DatagramPacket(message2.getBytes(), message2.length(), broadcast2, port2); //Création du datagramme UDP
            dgramSocket2.send(outPacket2); //Envoi de la notification de connexion
            dgramSocket2.close();

        }
        return "Fail sending UDP Packet";
    }

    public ArrayList<User> getUsers()
    {
        return this.users;
    }


}



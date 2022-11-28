package org.example;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.function.Predicate;

import static org.example.App.sendUDP;

public class UserManager {

    private ArrayList<User> users = new ArrayList<>();

    public UserManager () throws IOException {

        //Début notifications broadcast pour la connection

        //Fin notifications broadcast pour la connection


    }


    public void update(List<String> dataList) throws UnknownHostException {

        List<String> newData =  new ArrayList<String>(dataList);
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
            } else if (data[0].equals("m")) { //C'est unn changement de pseudo
                for(User n : this.users) //On verifie que le pseudo n'est pas déjà utilisé
                    if (n.getPseudo().equals(data[1])){
                        System.out.println("ERROR ---- The pseudo is already used.");
                        //Notifier en UDP pseudo deja utilisé
                    }

                for(User n : this.users) //On verifie que le pseudo n'est pas déjà utilisé
                    if (n.getIpAddress().equals(data[2])){
                        n.setPseudo(data[1]);
                        System.out.println("SUCCESS ---- The pseudo has been changed");
                    }

            }



        }





    }

    // Méthode permettant d'ajouter un utilisateur à la liste "users" et qui return un message confirmant l'ajout de cet utilisateur
    public String createUser(String pseudo, InetAddress ipAddress) {

        for(User n : this.users)
            if (n.getPseudo().equals(pseudo)){
                return "ERROR ---- The pseudo is already used.";
            }

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

        this.users.removeIf(user -> user.getPseudo().equals(pseudo) &&  user.getIpAddress().equals(ipAddress));

        for(User n : this.users)
            if (n.getPseudo().equals(pseudo)){
                return "ERROR ---- User : " + pseudo + " with @IP = " + ipAddress + " has NOT been deleted from the list of users";
            }
        return "SUCCESS ---- User : " + pseudo + " with @IP = " + ipAddress + " has been deleted from the list of users";
    }


}



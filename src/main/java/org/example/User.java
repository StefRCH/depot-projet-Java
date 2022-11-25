package org.example;

import java.net.InetAddress;

public class User {
    private String pseudo;
    private InetAddress ipAddress;

    public User(String pseudo, InetAddress ipAddress){
        this.pseudo = pseudo;
        this.ipAddress = ipAddress;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public InetAddress getIpAddress() {
        return this.ipAddress;
    }

    /*public String toString(int x){
        if (x == 0){
            return "Client " + this.pseudo + " has successfully logged into the app with @IP = " + this.ipAddress;
        }
        if (x == 1){
            return "Client " + this.pseudo + " logged out";

        }
    }*/
}

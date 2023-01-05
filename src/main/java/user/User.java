package user;

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

    public void setPseudo(String newPseudo) { this.pseudo = newPseudo; }
}

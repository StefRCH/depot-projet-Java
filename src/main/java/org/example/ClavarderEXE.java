package org.example;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClavarderEXE {
    public static void main(String Args[]) throws UnknownHostException {
        User user = new User("", InetAddress.getLocalHost());
        Login_Form LoGUI = new Login_Form(user);
        LoGUI.setVisible(true);
    }

}

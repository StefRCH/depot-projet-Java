package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

public class LoGUI implements ActionListener {
    public static JLabel userlabel;
    public static JTextField userText;
    public static JButton button;

    public static void main(String[] args) {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(350,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

        userlabel = new JLabel("User");
        userlabel.setBounds(10, 20, 80 ,25);
        panel.add(userlabel);

        userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        button = new JButton("Login");
        button.setBounds(10,80,80,25);
        button.addActionListener(new LoGUI());
        panel.add(button);





        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pseudo = userText.getText();
        System.out.println(pseudo);
        DatagramSocket dgramSocket = null; //Création d'un socket pour notifier la connection de l'utilisateur actuel
        try {
            dgramSocket = new DatagramSocket();
            String message = "c/" + pseudo; //Création du payload du paquet UDP
            InetAddress broadcast = InetAddress.getByName("10.1.255.255"); //Adresse destination !!Doit etre un broadcast !!
            int port = 4445; //Port de destination du broadcast
            DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), broadcast, port); //Création du datagramme UDP
            dgramSocket.send(outPacket); //Envoi de la notification de connexion
            dgramSocket.close(); //Fermeture du socket
        } catch (IOException socketException) {
            socketException.printStackTrace();
        }

    }
}

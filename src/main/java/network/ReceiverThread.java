package network;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiverThread extends Thread {
    /*private Thread thread; // contiendra le thread du client
    private Socket sock; // recevra le socket liant au client
    private BufferedReader in; // pour gestion du flux d'entrée

    //private BlablaServ blablaServ; // pour utilisation des méthodes de la classe principale
    private int numConv=0; // contiendra le numéro de client géré par ce thread

    // Constructeur : crée les éléments nécessaires au dialogue avec le client
    ReceiverThread(Socket s) throws IOException // On envoie le socket en parametre
    {
        this.sock=s; // passage de local en global
        InputStream inputStream = sock.getInputStream();

        // création d'une variable permettant l'utilisation du flux d'entrée avec des string
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        // ajoute le flux de sortie dans la liste et récupération de son numéro
        numConv +=1;

        this.thread = new Thread(this); // instanciation du thread
        //this.thread.start(); // démarrage du thread, la fonction run() est ici lancée
    }

    //** Methode :  exécutée au lancement du thread par t.start() **
    //** Elle attend les messages en provenance du serveur et les redirige **
    // cette méthode doit obligatoirement être implémentée à cause de l'interface Runnable
    public void run()
    {
        System.out.println("Thread receive lancé !");
        String message = ""; // déclaration de la variable qui recevra les messages du client
        // on indique dans la console la connexion d'un nouveau client
        try
        {
            // la lecture des données entrantes se fait caractère par caractère ...
            // ... jusqu'à trouver un caractère de fin de chaine
            String inputLine;
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //Création d'un format pour l'affichage de la date et l'heure avant chaque message
            Date date = new Date();
            while ((inputLine = in.readLine()) != null) {
                // Affichage du message sur la sortie standard
                System.out.println(s.format(date) + " ---- Message reçu : " + inputLine);
            }
        }
        catch (Exception e){ }

    }*/
    private Socket sock;

    public ReceiverThread(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        boolean quit = false;
        try {
            BufferedReader requete = new BufferedReader(new InputStreamReader(sock.getInputStream())); //J'isole le flux de comm en entrée (ce que l'on reçoit sur le serveur)
            System.out.println("Prêt à recevoir des messages :)");

            while (!quit) { //Tant que le client n'a pas demandé à quitter
                String newMessage = requete.readLine(); // Attente d'un message en entrée | ATTENTION --> readline() est bloquant

                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //Création d'un format pour l'affichage de la date et l'heure avant chaque message
                Date date = new Date();
                System.out.println(s.format(date) +" ---- Message reçue : " + newMessage);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
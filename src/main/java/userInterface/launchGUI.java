package userInterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.Input;
import network.TCPNetworkManager;
import network.UDPThread;
import user.UserManager;

import java.io.IOException;
import java.net.URL;


public class launchGUI extends Application implements EventHandler<ActionEvent> {

    @FXML
    private Button connexionButton;

    private static Stage primaryStage;
    private static VBox root;
    private static Stage mainStage;

    private static UserManager userManager;
    private static UDPThread udpThread;

    private static mainSceneController mainSceneController;
    private static TCPNetworkManager tcpNetworkManager;


    @FXML
    private Button addUser;

    @FXML
    private AnchorPane userComponent;
    public static void main(String[] args) throws IOException {


        /*System.out.println("Bienvenue sur votre application de chat ! Entrez votre pseudo : "); //Demande le pseudo à l'utilisateur

        String pseudo = myObj.nextLine();  //Lecture de l'entrée utilisateur;
        DatagramSocket dgramSocket = new DatagramSocket(); //Création d'un socket pour notifier la connection de l'utilisateur actuel
        String message = "c/" +pseudo; //Création du payload du paquet UDP
        InetAddress broadcast = InetAddress.getByName("127.0.0.1"); //Adresse destination !!Doit etre un broadcast !!
        int port = 4445; //Port de destination du broadcast
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),broadcast, port); //Création du datagramme UDP
        dgramSocket.send(outPacket); //Envoi de la notification de connexion
        dgramSocket.close(); */
        launch(args);
        /*while (true) {
            System.out.println("Pour vous deconnecter taper d, pour changer de pseudo taper m");
            String userPrompt = scanner.getNextLine();  //Lecture de l'entrée utilisateur;
            if (userPrompt.equals("d")) {
                userManager.sendUDP("d");
            } else if (userPrompt.equals("m")) {
                userManager.sendUDP("m");
            } else if (userPrompt.equals("gabu")) {
                System.out.println("C'est moi qui l'ai eu");
            }
        }*/

    }



    public void start(Stage stage) throws IOException {
            // initialize value of stage.
            tcpNetworkManager = new TCPNetworkManager();
            tcpNetworkManager.start();

            //Lancement d'UDP
            udpThread = new UDPThread();
            udpThread.start();

            //Création de l'User Manager au lancement de l'application
            userManager = udpThread.getUserManager();


            // Localisation du fichier FXML.
            final URL url = getClass().getResource("/logingScene.fxml");
            // Création du loader.
            final FXMLLoader fxmlLoader = new FXMLLoader(url);
            // Chargement du FXML.
            root = fxmlLoader.load();
            Scene loginScene = new Scene(root);
            root = (VBox) loginScene.getRoot();
            primaryStage = stage;
            primaryStage.setScene(loginScene);
            primaryStage.show();
    }

    public static void changeScene(Scene mainScene, String pseudo, FXMLLoader loader) throws IOException {



        root = (VBox) mainScene.getRoot();
        primaryStage.setScene(mainScene);
        primaryStage.show();

        Label pseudoField = (Label) root.lookup("#userPseudo");
        pseudoField.setText(pseudo);
        mainSceneController = loader.getController();


    }

    public static VBox getRoot() {
        return root;
    }
    public static void setRoot(VBox newRoot) {
        root = newRoot;
    }


    public static Stage getPrimaryStage() {
            return primaryStage;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static UDPThread getUdpThread() {
        return udpThread;
    }

    public static TCPNetworkManager getTcpNetworkManager() {
        return tcpNetworkManager;
    }

    public static mainSceneController getMainSceneController()
    {

        return mainSceneController;
    }

    @Override
            public void handle(ActionEvent actionEvent) {

        }

}

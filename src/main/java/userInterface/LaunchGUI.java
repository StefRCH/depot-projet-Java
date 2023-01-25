package userInterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.TCPNetworkManager;
import network.UDPThread;
import user.ConversationManager;
import user.UserManager;

import java.io.IOException;
import java.net.URL;


public class LaunchGUI extends Application implements EventHandler<ActionEvent> {

    @FXML
    private Button connexionButton;
    @FXML
    private static Stage primaryStage;
    private static VBox root;
    private static Stage mainStage;

    private static UserManager userManager;
    private static UDPThread udpThread;

    private static MainSceneController mainSceneController;
    private static TCPNetworkManager tcpNetworkManager;

    private static ConversationManager convManager;

    @FXML
    private AnchorPane userComponent;
    public static void main(String[] args) throws IOException {

        launch(args); //Lancement de l'interface graphique

    }



    public void start(Stage stage) throws IOException {

            //Creation des différentes instances de class de notre application
            tcpNetworkManager = new TCPNetworkManager();
            tcpNetworkManager.start();

            //Lancement d'UDP
            udpThread = new UDPThread();
            udpThread.start();

            //Création de l'User Manager au lancement de l'application
            userManager = new UserManager();
            udpThread.addObserver(userManager); //Ajout de UserManager comme observer a UDPThread

            convManager = new ConversationManager(userManager.getUsers());






            // Localisation du fichier FXML.
            final URL url = getClass().getResource("/logingScene.fxml");
            // Création du loader.

            final FXMLLoader fxmlLoader = new FXMLLoader(url);

            // Chargement du FXML.
            root = fxmlLoader.load();

            //Recuperation de notre classe controller
            LoginSceneController loginSceneController = fxmlLoader.getController();

            //Ajout d'un observer sur loginSceneController
            loginSceneController.addObserver(userManager);
            //Et inversement
            userManager.addObserver(loginSceneController);

            //Lancement graphique de la scene et recuperation du noeud parent (root)
            Scene loginScene = new Scene(root);
            root = (VBox) loginScene.getRoot();
            primaryStage = stage;
            primaryStage.setScene(loginScene);
            primaryStage.show();
    }

    public static void changeScene(Scene mainScene, String pseudo, FXMLLoader loader) throws IOException {

        // Methode permettant de changer de scene entre la scene du login et la scene principale de l'application

        //Recuperation du nouveau root
        root = (VBox) mainScene.getRoot();

        //Affichage de la nouvelle scene
        primaryStage.setScene(mainScene);
        primaryStage.show();

        //Affichage de notre pseudo en haut à gauche
        Label pseudoField = (Label) root.lookup("#userPseudo");
        pseudoField.setText(pseudo);

        //Recuperation de notre classe de controller
        mainSceneController = loader.getController();

        //Mise en place des observer
        mainSceneController.addObserver(userManager);
        userManager.addObserver(mainSceneController);

        //Connexion des observeurs des conversations et de la GUI
        convManager.addObserver(mainSceneController);
        mainSceneController.addObserver(convManager);


    }

    public static VBox getRoot() {
        return root;
    }


    public static Stage getPrimaryStage() {
            return primaryStage;
    }




    @Override
            public void handle(ActionEvent actionEvent) {

        }

}

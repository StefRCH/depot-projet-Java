package userInterface;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import user.UserManager;
import user.UserObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;




public class LoginSceneController implements Initializable, GraphicObservable, UserObserver {

    private ArrayList observerList; //Liste des observer de cette classe

    private Parent parent;
    private boolean pseudoCheck;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { //Executé lors du chargement de la scène

        this.observerList = new ArrayList();
        this.pseudoCheck = true;
    }

    public void loginButton(ActionEvent actionEvent) throws IOException {  //Handler activé lorsque l'on clique sur le bouton de connexion

        //Recuperation du root (noeud principal) de la scene
        this.parent = LaunchGUI.getRoot();

        //Recuperation de notre pseudo graphique grace a son id
        TextField pseudoField = (javafx.scene.control.TextField) parent.lookup("#pseudoField");
        System.out.println(pseudoField.getLength());
        System.out.println(pseudoField.getText().length());
        //Si pseudo fait de 1 a 20 caracteres
        if(pseudoField.getText().length() > 0 && pseudoField.getText().length() < 21)
        {
            //Notification a UserManager pour envoyer un paquet de connexion
            this.notifyObserver("connexion", pseudoField.getText());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.wrongPseudo("Pseudo must be at least 1 char long and under 21 char : ");
            return;
        }
        System.out.println("Je suis le test " + this.pseudoCheck);
        if (this.pseudoCheck) {
            //Chargement des ressources et changement de scène
            final URL url = getClass().getResource("/mainScene.fxml");
            // Création du loader.

            final FXMLLoader fxmlLoader = new FXMLLoader(url);
            // Chargement du FXML.

            final VBox root = (VBox) fxmlLoader.load();
            final Scene mainScene = new Scene(root);

            LaunchGUI.changeScene(mainScene,pseudoField.getText(), fxmlLoader);
        } else if (this.pseudoCheck == false)
        {
            this.wrongPseudo("Pseudo already used. Please choose another one : ");
        }





    }

    public void wrongPseudo(String message) { //Message d erreur a afficher en cas de mauvais pseudo


        AnchorPane loginPane = (AnchorPane) this.parent.lookup("#loginPane");
        Label oldLabel = (Label) this.parent.lookup("#wrongPseudo");
        if(oldLabel != null) //On verifie qu'il n'y ai pas deja un message d erreur, si il y en a un on l'enleve
        {
            Platform.runLater(new Runnable() { //Permet d'enlever le label graphiquement sans interrpompre le thread de JavaFX
                @Override
                public void run() {

                    loginPane.getChildren().remove(oldLabel);
                }
            });

        }


        Label pseudoLabel = new Label(message);
        pseudoLabel.setLayoutX(150);
        pseudoLabel.setLayoutY(90);
        pseudoLabel.setTextFill(Color.RED);
        pseudoLabel.setId("wrongPseudo");


        Platform.runLater(new Runnable() { //Permet d'ajouter le message graphiquement sans interrpompre le thread de JavaFX
            @Override
            public void run() {

                loginPane.getChildren().add(pseudoLabel);
            }
        });

    }


    @Override
    public void addObserver(GraphicObserver o) {
        observerList.add(o);
    } //Permet d'ajouter un observer

    @Override
    public void removeObserver(GraphicObserver o) {
        observerList.remove(o);
    } //Permet d'enlever un observer

    @Override
    public void notifyObserver(String action, String pseudo) { //Permet de notifier les observer

        //On parcours les observeurs, et on les notify un par un
        for (int i = 0; i < this.observerList.size(); i++) {
            GraphicObserver o = (GraphicObserver) observerList.get(i);
            try {
                o.updateFromGUI(action, pseudo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void updateFromUser(String action, String pseudo, String oldPseudo) {
        if(action == "wrongPseudo") {
            this.pseudoCheck = false;
        } if(action == "goodPseudo") {
            this.pseudoCheck = true;
        }
    }
}

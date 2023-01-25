package userInterface;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import network.Message;
import user.ConversationObserver;
import user.User;
import user.UserObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable,Cloneable, UserObserver, GraphicObservable, ConversationObserver {


    @FXML
    private AnchorPane userInfo;
    @FXML
    private VBox userPane;

    private Parent parent;
    private ArrayList observerList;
    @FXML
    private AnchorPane convPane;
    @FXML
    private TextField inputTextField;
    private boolean pseudoCheck;

    public void addUser(String pseudo) { //Méthode permettant d'ajouter un User de manière graphique

        //Recuperation du root et initialisation de l'userPane et du convPane (A CHANGER DE PLACE SINON APPLI CRASH QUAND ON EST SEUL CONNECTE)
        this.pseudoCheck = true;
        this.parent = LaunchGUI.getRoot();
        this.userPane = (VBox) this.parent.lookup("#userPane");
        this.convPane = (AnchorPane) this.parent.lookup("#convPane");
        this.inputTextField = (TextField) this.parent.lookup("#inputTextField"); //Zone ou on ecrit


        //Creation d'un userInfoPane grapique
        AnchorPane userInfo = userInfoPane(pseudo);


        Platform.runLater(new Runnable() { //Permet d'ajouter le UserInfoPane graphiquement sans interrpompre le thread de JavaFX
            @Override
            public void run() {

                userPane.getChildren().add(userInfo);
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { //Methode qui se lance a l'initialisation de la scene
        this.observerList = new ArrayList(); //Creation de la list d'observer


    }

    public void logout(ActionEvent actionEvent) { //Handler qui se lance quand on clique sur le bouton de deconnexion

        //Notification a UserManager de la deconnexion
        this.notifyObserver("deconnexion", "");

    }

    public void removeUser(String pseudo) //Methode appelée lors qu'un User est retiré de la liste
    {

        Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX, on parcours la liste des userPane pour voir quel pseudo doit etre retiré (RemoveIF)
            @Override
            public void run() {

                userPane.getChildren().removeIf( node -> node.getId().equals(pseudo));
            }
        });

    }

    public void initiateConversation(MouseEvent mouseEvent) { //Methode qui permet d'initier une connexion TCP quand on clique sur quelqu'un

        //On recupere le pseudo de celui sur qui on a cliqué
        AnchorPane user = (AnchorPane) mouseEvent.getSource();
        Label userLabel = (Label) user.getChildren().get(0);
        String pseudo = userLabel.getText();

        //On notify le TCPManager
        this.notifyObserver("initiateConv", pseudo);

        this.createConvPane(pseudo);
    }

    private void createConvPane(String pseudo) {
        ScrollPane messagePane = new ScrollPane();
        messagePane.setLayoutX(11);
        messagePane.setLayoutY(10);
        messagePane.setMinWidth(960);
        messagePane.setMinHeight(580);
        messagePane.setId(pseudo+"ConvPane");

        FlowPane messagePane2 = new FlowPane();
        messagePane2.setLayoutX(11);
        messagePane2.setLayoutY(10);
        messagePane2.setMinWidth(960);
        messagePane2.setMinHeight(580);

        messagePane.setContent(messagePane2);




        this.convPane.getChildren().add(messagePane);
    }

    public void messageReceived(String pseudo, Message message) {
        System.out.println("dans messageReceived 1");

        if(this.convPane.lookup("#"+pseudo+"ConvPane") != null) {
            System.out.println("dans messageReceived 2");

            ScrollPane actualMessageScrollPane = (ScrollPane) this.convPane.lookup("#"+pseudo+"ConvPane");
            Label graphicMessage = this.createGraphicMessage(message);
            Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX, on ajoute le message en dessous
                @Override
                public void run() {
                    FlowPane messagePane = (FlowPane) actualMessageScrollPane.getContent();
                    messagePane.getChildren().add(graphicMessage);

                }
            });
        } else
        {
            //Faire une notif
        }
    }

    private Label createGraphicMessage(Message message) {
        Label graphicMessage = new Label();
        graphicMessage.setText(message.getDate() + "\n" + message.getPayload());
        return graphicMessage;
    }

    public void  changePseudo(ActionEvent actionEvent) { //Methode permettant le changement de pseudo. Se declenche quand on clique sur le bouton ChangePseudo

        //Creation d'un label a afficher au dessus de la zone de texte des messages
        Label changePseudoLabel = new Label("Enter your new pseudo and click on validate : ");
        changePseudoLabel.setId("changePseudoLabel");

        //Permet de le placer au bon endroit
        changePseudoLabel.setLayoutY(590);
        changePseudoLabel.setLayoutX(16);

        //Ajout du label dans la convPane
        this.convPane.getChildren().add(changePseudoLabel);

        //On fait en sorte qu'un bouton Validate remplace le bouton Change Pseudo
        //On garde le meme mais on change ses propriétés
        Button changePseudoButton = (Button) this.parent.lookup("#changePseudoButton");
        changePseudoButton.setText("Validate");
        changePseudoButton.setMinWidth(119);

        //On lui définit un nouveau Handler a activer quand on clique dessus
        changePseudoButton.setOnAction(event ->{ //S'activera quand on cliquera sur le bouton Validate

            //On enleve le label qu'on vient de creer
            this.convPane.getChildren().remove(changePseudoLabel);

            //On recupere ce que l'user a rentré comme pseudo
            String newPseudo = this.inputTextField.getText();


            //Verification du pseudo
            if(newPseudo.length() > 0 && newPseudo.length() < 21) //On test la longueur du pseudo rentré
            {
                try {
                    this.notifyObserver("checkUser", newPseudo);
                    Thread.sleep(1000); //On attend pour verifier si on recoit pas un paquet nous indiquant un mauvais pseudo
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                this.wrongPseudo("Pseudo must be at least 1 char long and under 21 char : ");
                return;
            }
            System.out.println("Je suis le test " + this.pseudoCheck);
            if (this.pseudoCheck) {

                //On retire le message d'erreur s'il existe
                Label oldLabel = (Label) this.parent.lookup("#wrongPseudo");
                if(oldLabel != null) //On verifie qu'il n'y ai pas deja un message d erreur, si il y en a un on l'enleve
                {
                    Platform.runLater(new Runnable() { //Permet d'enlever le label graphiquement sans interrpompre le thread de JavaFX
                        @Override
                        public void run() {

                            convPane.getChildren().remove(oldLabel);
                        }
                    });

                }

                //On notifie le changement de pseudo
                this.notifyObserver("changePseudo", newPseudo); //Si tout est bon on change de pseudo

            } else if (this.pseudoCheck == false) //Si on a recu un paquet W, alors le pseudo est deja utilisé
            {
                this.wrongPseudo("Pseudo already used. Please choose another one : ");
                return;
            }

            //On remet le bon intitulé au bouton
            changePseudoButton.setText("Change pseudo");

            //On clear la zone de text
            this.inputTextField.clear();
            this.inputTextField.setPromptText("Enter your message and press enter");

            //On modifie le pseudo en haut a gauche
            Label userPseudo = (Label) this.parent.lookup("#userPseudo");
            userPseudo.setText(newPseudo);

            //On remet l'handler de base au bouton changePseuo
            changePseudoButton.setOnAction(this::changePseudo);


            }
        );


    }

    public void wrongPseudo(String message) { //Message d erreur a afficher en cas de mauvais pseudo


        Label oldLabel = (Label) this.parent.lookup("#wrongPseudo");
        if(oldLabel != null) //On verifie qu'il n'y ai pas deja un message d erreur, si il y en a un on l'enleve
        {
            Platform.runLater(new Runnable() { //Permet d'enlever le label graphiquement sans interrpompre le thread de JavaFX
                @Override
                public void run() {

                    convPane.getChildren().remove(oldLabel);
                }
            });

        }


        Label pseudoLabel = new Label(message);
        pseudoLabel.setLayoutX(16);
        pseudoLabel.setLayoutY(590);
        pseudoLabel.setTextFill(Color.RED);
        pseudoLabel.setId("wrongPseudo");


        Platform.runLater(new Runnable() { //Permet d'ajouter le message graphiquement sans interrpompre le thread de JavaFX
            @Override
            public void run() {

                convPane.getChildren().add(pseudoLabel);
            }
        });

    }

    public void changePseudo(String pseudo, String oldPseudo) {

        Label pseudoChanged = (Label) this.parent.lookup("#" + oldPseudo+"Label"); //Recuperation de l'ancien pseudo
        System.out.println(pseudoChanged);
        Platform.runLater(new Runnable() { //Permet d'ajouter le message graphiquement sans interrpompre le thread de JavaFX
            @Override
            public void run() {

                pseudoChanged.setText(pseudo); //Mise a jour du pseudo
                pseudoChanged.setId(pseudo+"Label");
            }
        });

        System.out.println(pseudoChanged.getText());

    }

    public AnchorPane userInfoPane(String pseudo) //Permet de creer le "carré" graphique pour ajouter un user
    {

        //On creer l'anchorpane
        AnchorPane userInfo = new AnchorPane();
        userInfo.setMinWidth(222);
        userInfo.setMinHeight(72);
        userInfo.setStyle("-fx-border-color: crimson; -fx-border-width: 4;");
        userInfo.setId(pseudo); //Important car ca facilitera le fait de le retrouver pour le supprimer plus tard

        //On creer le label a partir du pseudo
        Label pseudoLabel = new Label("");
        pseudoLabel.setLayoutX(76);
        pseudoLabel.setLayoutY(20);
        pseudoLabel.prefHeight(33);
        pseudoLabel.prefWidth(120);
        pseudoLabel.setText(pseudo);
        pseudoLabel.setId(pseudo+"Label");


        //On charge l'avatar
        Image imageAvatar = new Image("/avatar.png");
        ImageView avatar = new ImageView(imageAvatar);
        avatar.setFitHeight(54);
        avatar.setFitWidth(38);
        avatar.setLayoutX(14);
        avatar.setLayoutY(11);
        avatar.setPickOnBounds(true);
        avatar.setPreserveRatio(true);

        //On imbrique les différent éléments dans l'anchorPane
        userInfo.getChildren().add(pseudoLabel);
        userInfo.getChildren().add(avatar);

        userInfo.setOnMouseClicked(this::initiateConversation);

        //On retourne le tout
        return userInfo;

    }





    @Override
    public void addObserver(GraphicObserver o) {
        observerList.add(o);
    } //Ajoute un observer

    @Override
    public void removeObserver(GraphicObserver o) {
        observerList.remove(o);
    } //retire un observer

    @Override
    public void notifyObserver(String action, String pseudo) { //Permet de notifier les observers, ici UserManager
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
        if(action.equals("add")) { //Si l'observable (UserManager) notify avec add, alors j'ajoute un nouvel utilisateur graphique
            addUser(pseudo);
        } else if (action.equals("remove")) {
            removeUser(pseudo); //Si l'observable (UserManager) notify avec remove, alors je supprime un utilisateur graphique
        } else if (action.equals("changePseudo")) {
            this.changePseudo(pseudo, oldPseudo);
        } else if(action == "wrongPseudo") {
            this.pseudoCheck = false;
        } else if(action == "goodPseudo") {
            this.pseudoCheck = true;
        }
    }

    @Override
    public void updateFromConv(String action, String pseudo, Message message) {
        if(action.equals("newMessage")) { //Si l'observable (UserManager) notify avec add, alors j'ajoute un nouvel utilisateur graphique
            System.out.println("dans updatefromConv");
            this.messageReceived(pseudo, message);
        }
    }
}

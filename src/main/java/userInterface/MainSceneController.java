package userInterface;

import BDD.DataBaseJava;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import network.Message;
import user.ConversationObserver;
import user.User;
import user.UserManager;
import user.UserObserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable,Cloneable, UserObserver, GraphicObservable, ConversationObserver {


    @FXML
    private AnchorPane userInfo;
    @FXML
    private VBox userPane;

    private Parent parent;

    private String actualPseudoConv;
    private ArrayList observerList;
    @FXML
    private AnchorPane convPane;
    @FXML
    private TextField inputTextField;
    private boolean pseudoCheck;

    private ArrayList<ScrollPane> convPaneList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { //Methode qui se lance a l'initialisation de la scene
        this.observerList = new ArrayList(); //Creation de la list d'observer


    }
    public void initVariable() {
        this.pseudoCheck = true;
        this.parent = LaunchGUI.getRoot();
        this.userPane = (VBox) this.parent.lookup("#userPane");
        this.convPane = (AnchorPane) this.parent.lookup("#convPane");
        this.inputTextField = (TextField) this.parent.lookup("#inputTextField"); //Zone ou on ecrit
        this.convPaneList = new ArrayList<ScrollPane>();
    }

    public void addUser(String pseudo) { //Méthode permettant d'ajouter un User de manière graphique

        //Recuperation du root et initialisation de l'userPane et du convPane (A CHANGER DE PLACE SINON APPLI CRASH QUAND ON EST SEUL CONNECTE)



        //Creation d'un userInfoPane grapique
        AnchorPane userInfo = userInfoPane(pseudo);


        Platform.runLater(new Runnable() { //Permet d'ajouter le UserInfoPane graphiquement sans interrpompre le thread de JavaFX
            @Override
            public void run() {

                userPane.getChildren().add(userInfo);
            }
        });
    }




    public void logout(ActionEvent actionEvent) { //Handler qui se lance quand on clique sur le bouton de deconnexion

        //Notification a UserManager de la deconnexion
        this.notifyObserver("deconnexionConv", "", null);
        this.notifyObserver("deconnexion", "", null);

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

    synchronized public void initiateConversation(MouseEvent mouseEvent) { //Methode qui permet d'initier une connexion TCP quand on clique sur quelqu'un


        //On recupere le pseudo de celui sur qui on a cliqué
        AnchorPane user = (AnchorPane) mouseEvent.getSource();
        Label userLabel = (Label) user.getChildren().get(0);
        String pseudo = userLabel.getText();
        this.actualPseudoConv = pseudo;
        for(ScrollPane convIndex : convPaneList) {
            System.out.println(convIndex.getId());

            if(convIndex.getId().equals(pseudo+"ConvPane")) {

                Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX
                    @Override
                    public void run() {


                        try {

                            Label compteur = (Label) parent.lookup("#"+pseudo+"CompteurLabel"); //On remet a 0 le compteur
                            compteur.setText(""); //On remet a 0 le compteur

                            convPane.getChildren().remove(5); //On enleve le pane de discussion actuel
                            convPane.getChildren().add(convIndex); //On ajoute celui sur lequel il a cliqué
                        } catch (IndexOutOfBoundsException e) {
                            Label compteur = (Label) parent.lookup("#"+pseudo+"CompteurLabel"); //On remet a 0 le compteur
                            compteur.setText("");

                            convPane.getChildren().add(convIndex); //On ajoute celui sur lequel il a cliqué

                        }
                    }
                });
                return;
            }
        }


        //On notify le TCPManager
        this.notifyObserver("initiateConv", pseudo, null);


        ScrollPane conv = this.createConvPane(pseudo);
        this.convPaneList.add(conv);
        Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX
            @Override
            public void run() {

                convPane.getChildren().add(conv);
            }
        });


    }

    private ScrollPane createConvPane(String pseudo) {
        ScrollPane messagePane = new ScrollPane();
        messagePane.setLayoutX(11);
        messagePane.setLayoutY(10);
        messagePane.setMinWidth(960);
        messagePane.setMinHeight(580);
        messagePane.setMaxWidth(960);
        messagePane.setMaxHeight(580);
        messagePane.setId(pseudo+"ConvPane");

        VBox layoutMessage = new VBox();
        layoutMessage.setLayoutX(11);
        layoutMessage.setLayoutY(10);
        layoutMessage.setMinWidth(950);
        layoutMessage.setMinHeight(570);

        messagePane.setContent(layoutMessage);



        return messagePane;
    }

    public void messageReceived(String pseudo, Message message) {
        AnchorPane graphicMessage = this.createGraphicMessage(message, "receive");
        HBox hBox=new HBox();
        hBox.getChildren().add(graphicMessage);
        hBox.setAlignment(Pos.BASELINE_LEFT);
        if(this.convPane.lookup("#"+pseudo+"ConvPane") != null) {

            ScrollPane actualMessageScrollPane = (ScrollPane) this.convPane.lookup("#"+pseudo+"ConvPane");
            VBox messagePane = (VBox) actualMessageScrollPane.getContent();

            Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX, on ajoute le message en dessous
                @Override
                public void run() {

                    messagePane.getChildren().add(hBox);
                    messagePane.setSpacing(10);

                }
            });
            return;
        } else {
            Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX, on ajoute le message en dessous
                @Override
                public void run() {

                    Label compteur = (Label) parent.lookup("#"+pseudo+"CompteurLabel"); //On augmente le compteur
                    if(!compteur.getText().equals("")) {
                        int nombreCompteur = Integer.parseInt(compteur.getText());
                        nombreCompteur += 1;
                        compteur.setText(Integer.toString(nombreCompteur));
                    } else {
                        compteur.setText("1");
                    }

                }
            });

            for(ScrollPane conv : convPaneList) {
               System.out.println(conv.getId());
               if(conv.getId().equals(pseudo+"ConvPane")) {
                   VBox messagePane = (VBox) conv.getContent();
                   Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX, on ajoute le message en dessous
                       @Override
                       public void run() {

                           messagePane.getChildren().add(hBox);
                           messagePane.setSpacing(10);

                       }
                   });
                    return;
               }
           }

            ScrollPane convPane = this.createConvPane(pseudo);
            this.convPaneList.add(convPane);
            VBox messagePane = (VBox) convPane.getContent();
            Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX, on ajoute le message en dessous
                @Override
                public void run() {

                    messagePane.getChildren().add(hBox);
                    messagePane.setSpacing(10);

                }
            });
        }

    }

    private AnchorPane createGraphicMessage(Message message, String info) {
        Label graphicMessage = new Label();
        if(info.equals("send"))
            graphicMessage.setText(message.getDate() + " : Sent" + "\n" + message.getPayload());
        else if(info.equals("receive"))
            graphicMessage.setText(message.getDate() + " : Received"+"\n" + message.getPayload());

        AnchorPane messagePane = new AnchorPane();
        messagePane.getChildren().add(graphicMessage);
        return messagePane;
    }

    public void sendMessage(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER))
        {
            if(this.inputTextField.getText() != null && !this.inputTextField.equals("")) {
                Date date = new Date();
                Message message = new Message(this.inputTextField.getText(), date);
                this.inputTextField.clear();
                System.out.println("dans le sendMessage GUI");
                System.out.println(message.getPayload());
                this.showSendingMessage(message);
                this.notifyObserver("sendMessage", this.actualPseudoConv, message);
            }
        }
    }

    private void showSendingMessage(Message message) {
        AnchorPane graphicMessage = this.createGraphicMessage(message, "send");
        graphicMessage.setLayoutX(400);
        ScrollPane actualMessageScrollPane = (ScrollPane) this.convPane.lookup("#"+this.actualPseudoConv+"ConvPane");
        Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX, on ajoute le message en dessous
            @Override
            public void run() {
                VBox messagePane = (VBox) actualMessageScrollPane.getContent();
                HBox hBox=new HBox();
                hBox.getChildren().add(graphicMessage);
                hBox.setAlignment(Pos.BASELINE_RIGHT);
                messagePane.getChildren().add(hBox);
                messagePane.setSpacing(10);

            }
        });
    }
    private void deconnexionUser(String pseudo) { //Gere la deconnexion d'une personne

        if(this.convPane.lookup("#"+pseudo+"ConvPane") != null) {

            ScrollPane actualMessageScrollPane = (ScrollPane) this.convPane.lookup("#"+pseudo+"ConvPane");

            Platform.runLater(new Runnable() { //Méthode pour pas interrompre le thread javaFX, on ajoute le message en dessous
                @Override
                public void run() {

                    convPane.getChildren().remove(actualMessageScrollPane);
                    convPaneList.remove(actualMessageScrollPane);
                }
            });
            return;
        } else {
            for(ScrollPane conv : convPaneList) {
                if (conv.getId().equals(pseudo + "ConvPane")) {
                    ScrollPane scrollPaneToRemove = conv;
                    this.convPaneList.remove(conv);
                    break;

                }
            }
        }
        this.notifyObserver("deconnexionUser", pseudo, null);
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
                    this.notifyObserver("checkUser", newPseudo, null);
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
                this.notifyObserver("changePseudo", newPseudo, null); //Si tout est bon on change de pseudo

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
            userPseudo.setText(newPseudo + " (moi)");

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

        Label compteurLabel = new Label("");
        compteurLabel.setLayoutX(190);
        compteurLabel.setLayoutY(47);
        compteurLabel.prefHeight(33);
        compteurLabel.prefWidth(120);
        compteurLabel.setId(pseudo+"CompteurLabel");




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
        userInfo.getChildren().add(compteurLabel);

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
    synchronized public void notifyObserver(String action, String pseudo, Message message) { //Permet de notifier les observers, ici UserManager
        for (int i = 0; i < this.observerList.size(); i++) {
            GraphicObserver o = (GraphicObserver) observerList.get(i);
            try {
                o.updateFromGUI(action, pseudo, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    synchronized public void updateFromUser(String action, String pseudo, String oldPseudo) {
        if(action.equals("add")) { //Si l'observable (UserManager) notify avec add, alors j'ajoute un nouvel utilisateur graphique
            addUser(pseudo);
        } else if (action.equals("remove")) {
            removeUser(pseudo); //Si l'observable (UserManager) notify avec remove, alors je supprime un utilisateur graphique
        } else if (action.equals("changePseudo")) {
            this.changePseudo(pseudo, oldPseudo);
        } else if(action.equals("wrongPseudo")) {
            this.pseudoCheck = false;
        } else if(action.equals("goodPseudo")) {
            this.pseudoCheck = true;
        } else if(action.equals("deconnexionUser")) {
            this.deconnexionUser(pseudo);
        }
    }


    @Override
    synchronized public void updateFromConv(String action, String pseudo, Message message) {
        if(action.equals("newMessage")) { //Si l'observable (UserManager) notify avec add, alors j'ajoute un nouvel utilisateur graphique
            System.out.println("dans updatefromConv");
            this.messageReceived(pseudo, message);

        } else if(action.equals("newConv"))
        {
            System.out.println("dans updatefromConv2");
            ScrollPane conv = this.createConvPane(pseudo);
            this.convPaneList.add(conv);

        }
    }



}

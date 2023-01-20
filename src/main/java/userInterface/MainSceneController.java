package userInterface;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import user.User;
import user.UserObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable,Cloneable, UserObserver, GraphicObservable {

    @FXML
    private Button addUserButton;
    @FXML
    private AnchorPane userInfo;
    @FXML
    private VBox userPane;

    private Parent parent;
    private ArrayList observerList;
    @FXML
    private AnchorPane convPane;

    public void addUser(String pseudo) {

        this.parent = LaunchGUI.getRoot();
        this.userPane = (VBox) this.parent.lookup("#userPane");
        this.convPane = (AnchorPane) this.parent.lookup("#convPane");


        AnchorPane userInfo = userInfoPane(pseudo);
        System.out.println(this.userPane.getChildren());

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                userPane.getChildren().add(userInfo);
            }
        });
    }

    public void deleteUser(String pseudo)  {


        AnchorPane toRemove = (AnchorPane) this.parent.lookup(pseudo);
        this.userPane.getChildren().remove(toRemove);
        LaunchGUI.getPrimaryStage().show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.observerList = new ArrayList();


    }

    public void logout(ActionEvent actionEvent) {
        this.notifyObserver("deconnexion", "");

    }

    public void removeUser(String pseudo)
    {
        for (int i = 0; i < this.userPane.getChildren().size(); i++) {
            System.out.println(this.userPane.getChildren().get(i).getId());
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                userPane.getChildren().removeIf( node -> node.getId().equals(pseudo));
            }
        });

    }

    public void  changePseudo(ActionEvent actionEvent) {

        Label changePseudoLabel = new Label("Enter your new pseudo and click on validate : ");
        changePseudoLabel.setId("changePseudoLabel");
        changePseudoLabel.setLayoutY(580);
        changePseudoLabel.setLayoutX(16);
        this.convPane.getChildren().add(changePseudoLabel);
        Button changePseudoButton = (Button) this.parent.lookup("#changePseudoButton");
        changePseudoButton.setText("Validate");
        changePseudoButton.setMinWidth(119);
        changePseudoButton.setOnAction(event ->{

            this.convPane.getChildren().remove(changePseudoLabel);
            TextField inputTextField = (TextField) this.parent.lookup("#inputTextField");
            changePseudoButton.setText("Change pseudo");
            String newPseudo = inputTextField.getText();
            this.notifyObserver("changePseudo", newPseudo);
            inputTextField.clear();
            inputTextField.setPromptText("Enter your message and press enter");
            Label userPseudo = (Label) this.parent.lookup("#userPseudo");
            userPseudo.setText(newPseudo);
            changePseudoButton.setOnAction(this::changePseudo);


            }
        );


    }

    public void validatePseudo(ActionEvent actionEvent) {





    }

    public AnchorPane userInfoPane(String pseudo)
    {
        AnchorPane userInfo = new AnchorPane();
        userInfo.setMinWidth(222);
        userInfo.setMinHeight(72);
        userInfo.setStyle("-fx-border-color: crimson; -fx-border-width: 4;");
        userInfo.setId(pseudo);


        Label pseudoLabel = new Label("");
        pseudoLabel.setLayoutX(76);
        pseudoLabel.setLayoutY(20);
        pseudoLabel.prefHeight(33);
        pseudoLabel.prefWidth(120);
        pseudoLabel.setText(pseudo);
        pseudoLabel.setId(pseudo+"Label");



        Image imageAvatar = new Image("/avatar.png");
        ImageView avatar = new ImageView(imageAvatar);
        avatar.setFitHeight(54);
        avatar.setFitWidth(38);
        avatar.setLayoutX(14);
        avatar.setLayoutY(11);
        avatar.setPickOnBounds(true);
        avatar.setPreserveRatio(true);

        userInfo.getChildren().add(pseudoLabel);
        userInfo.getChildren().add(avatar);


        return userInfo;

    }


    @Override
    public void update(String action, String pseudo) {
        if(action.equals("add")) {
            addUser(pseudo);
        } else if (action.equals("remove")) {
            removeUser(pseudo);
        }
    }

    @Override
    public void addObserver(GraphicObserver o) {
        observerList.add(o);
    }

    @Override
    public void removeObserver(GraphicObserver o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObserver(String action, String pseudo) {
        for (int i = 0; i < this.observerList.size(); i++) {
            GraphicObserver o = (GraphicObserver) observerList.get(i);
            try {
                o.update(action, pseudo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}

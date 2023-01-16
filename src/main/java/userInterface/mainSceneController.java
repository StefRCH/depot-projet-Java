package userInterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import user.User;
import user.UserManager;

import java.awt.*;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class mainSceneController implements Initializable,Cloneable {



    @FXML
    private Button addUserButton;
    @FXML
    private AnchorPane userInfo;
    @FXML
    private VBox userPane;

    private UserManager userManager;

    private ArrayList<User> users = new ArrayList<>();

    private Parent parent;

    public void addUser(String pseudo) {

        this.parent = launchGUI.getRoot();

        this.userPane = (VBox) this.parent.lookup("#userPane");

        AnchorPane userInfo = userInfoPane(pseudo);

        System.out.println(this.userPane.getChildren());
        this.userPane.getChildren().add(userInfo);
        launchGUI.getPrimaryStage().show();
    }

    public void deleteUser(String pseudo)  {

        AnchorPane toRemove = (AnchorPane) this.parent.lookup(pseudo);
        this.userPane.getChildren().remove(toRemove);
        launchGUI.getPrimaryStage().show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userManager = launchGUI.getUserManager();
        this.users = this.userManager.getUsers();

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
}

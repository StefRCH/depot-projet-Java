package userInterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

import java.awt.*;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainSceneController implements Initializable,Cloneable {



    @FXML
    private Button addUserButton;
    @FXML
    private AnchorPane userInfo;
    @FXML
    private VBox userPane;

    public void addUser(ActionEvent actionEvent) throws CloneNotSupportedException {
        Parent parent = launchGUI.getRoot();
        this.addUserButton = (Button) parent.lookup("#addUser");
        this.userPane = (VBox) parent.lookup("#userPane");

        AnchorPane userInfo = userInfoPane();

        System.out.println(this.userPane.getChildren());
        this.userPane.getChildren().add(userInfo);
        launchGUI.getPrimaryStage().show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public AnchorPane userInfoPane()
    {
        AnchorPane userInfo = new AnchorPane();
        userInfo.setMinWidth(222);
        userInfo.setMinHeight(72);
        userInfo.setStyle("-fx-border-color: crimson; -fx-border-width: 4;");


        Label pseudo = new Label("");
        pseudo.setLayoutX(76);
        pseudo.setLayoutY(20);
        pseudo.prefHeight(33);
        pseudo.prefWidth(120);
        pseudo.setText("HELLO");



        Image imageAvatar = new Image("/avatar.png");
        ImageView avatar = new ImageView(imageAvatar);
        avatar.setFitHeight(54);
        avatar.setFitWidth(38);
        avatar.setLayoutX(14);
        avatar.setLayoutY(11);
        avatar.setPickOnBounds(true);
        avatar.setPreserveRatio(true);

        userInfo.getChildren().add(pseudo);
        userInfo.getChildren().add(avatar);


        return userInfo;

    }
}

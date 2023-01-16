package userInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import user.User;
import user.UserManager;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;




public class loginSceneController implements Initializable {

    private UserManager userManager;

    public void loginButton(ActionEvent actionEvent) throws IOException {

        Parent parent = launchGUI.getRoot();
        System.out.println(parent);

        TextField pseudoField = (javafx.scene.control.TextField) parent.lookup("#pseudoField");
        if(pseudoField != null)
        {
            this.userManager.sendUDPConnexion(pseudoField.getText());
        }



        final URL url = getClass().getResource("/mainScene.fxml");
        // Création du loader.

        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        // Chargement du FXML.

        final VBox root = (VBox) fxmlLoader.load();
        final Scene mainScene = new Scene(root);
        this.userManager.setMainSceneController(fxmlLoader.getController());
        launchGUI.changeScene(mainScene,pseudoField.getText(), fxmlLoader);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userManager = launchGUI.getUserManager();
    }
}

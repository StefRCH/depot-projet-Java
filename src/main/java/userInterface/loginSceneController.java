package userInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;




public class loginSceneController implements Initializable {


    public void loginButton(ActionEvent actionEvent) throws IOException {

        final URL url = getClass().getResource("/mainScene.fxml");
        // Création du loader.
        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        // Chargement du FXML.
        final VBox root = (VBox) fxmlLoader.load();
        final Scene mainScene = new Scene(root);
        launchGUI.changeScene(mainScene);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

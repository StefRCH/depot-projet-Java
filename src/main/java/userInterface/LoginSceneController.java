package userInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import user.UserManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;




public class LoginSceneController implements Initializable, GraphicObservable {

    private ArrayList observerList;

    public void loginButton(ActionEvent actionEvent) throws IOException {

        Parent parent = LaunchGUI.getRoot();
        System.out.println(parent);

        TextField pseudoField = (javafx.scene.control.TextField) parent.lookup("#pseudoField");
        if(pseudoField != null)
        {
            this.notifyObserver("connexion", pseudoField.getText());
        }



        final URL url = getClass().getResource("/mainScene.fxml");
        // Création du loader.

        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        // Chargement du FXML.

        final VBox root = (VBox) fxmlLoader.load();
        final Scene mainScene = new Scene(root);

        LaunchGUI.changeScene(mainScene,pseudoField.getText(), fxmlLoader);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.observerList = new ArrayList();
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

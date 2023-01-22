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

    private ArrayList observerList; //Liste des observer de cette classe


    public void loginButton(ActionEvent actionEvent) throws IOException {  //Handler activé lorsque l'on clique sur le bouton de connexion

        //Recuperation du root (noeud principal) de la scene
        Parent parent = LaunchGUI.getRoot();

        //Recuperation de notre pseudo graphique grace a son id
        TextField pseudoField = (javafx.scene.control.TextField) parent.lookup("#pseudoField");

        //Si pseudo fait de 1 a 20 caracteres
        if(pseudoField.getText().length() > 0 && pseudoField.getText().length() < 21)
        {
            //Notification a UserManager pour envoyer un paquet de connexion
            this.notifyObserver("connexion", pseudoField.getText());
        } else {
            //CREER UNE ERREUR ICI QUI S AFFICHE GRAPHIQEUEMENT
            System.out.println("Pseudo trop court on trop long");
        }


        //Chargement des ressources et changement de scène
        final URL url = getClass().getResource("/mainScene.fxml");
        // Création du loader.

        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        // Chargement du FXML.

        final VBox root = (VBox) fxmlLoader.load();
        final Scene mainScene = new Scene(root);

        LaunchGUI.changeScene(mainScene,pseudoField.getText(), fxmlLoader);


    }

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) { //Executé lors du chargement de la scène

        this.observerList = new ArrayList();
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
                o.update(action, pseudo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

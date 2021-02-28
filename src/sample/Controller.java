package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Controller {

    ClientBackend.Client client;

    @FXML
    BorderPane mainPane;
    @FXML
    Button loginButton;
    @FXML
    BorderPane sideBarPane;

    public void loginBtnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        AnchorPane pane = loader.load();
        mainPane.setCenter(pane);
        Login controller = loader.getController();
        controller.sidePane = sideBarPane;
        controller.mainPane = mainPane;
        controller.client = this.client;
    }

    public void signUpAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp.fxml"));
        AnchorPane pane = loader.load();
        SignUp signUp = loader.getController();
        signUp.client = this.client;
        mainPane.setCenter(pane);
    }

    public void aboutAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
        AnchorPane pane = loader.load();
        mainPane.setCenter(pane);
    }

    public void helpAction(ActionEvent actionEvent) {
    }

}

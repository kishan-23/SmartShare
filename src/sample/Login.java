package sample;

import AfterLogin.AfterLoginHome;
import AfterLogin.SideBar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Login {

    BackEnd.Client client;

    @FXML
    TextField idField;
    @FXML
    PasswordField passField;
    @FXML
    Button loginBtn;
    @FXML
    Label msgLabel;
    @FXML
    BorderPane sidePane;
    @FXML
    BorderPane mainPane;

    public void loginAction(ActionEvent actionEvent) throws IOException {
        msgLabel.setText("ID: "+idField.getText()+"\nPass: "+passField.getText());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../AfterLogin/sideBar.fxml"));
        AnchorPane pane = loader.load();
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("../AfterLogin/afterLoginHome.fxml"));
        AnchorPane pane2 = loader2.load();
        sidePane.setTop(pane);
        mainPane.setCenter(pane2);
        SideBar loadercontroller = loader.getController();
        loadercontroller.mainPane = mainPane;
        AfterLoginHome cntrl = loader2.getController();
        cntrl.idLabel.setText(idField.getText());
        //xf();
    }
}

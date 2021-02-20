package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUp {
    @FXML Label msgLabel;
    @FXML PasswordField cnfPassField;
    @FXML PasswordField passField;
    @FXML TextField idField;
    @FXML TextField emailField;
    @FXML TextField nameField;

    BackEnd.Client client;

}

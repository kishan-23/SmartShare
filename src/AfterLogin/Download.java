package AfterLogin;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Download {
    public TextField keyField;
    public TextField pathField;
    public Label msgLabel;

    public void browseBtnAction(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        File f = chooser.showDialog(new Stage());
        pathField.setText(f.getPath());
    }

    public void downloadBtnAction(ActionEvent actionEvent) {
    }
}

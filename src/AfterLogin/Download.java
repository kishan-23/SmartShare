package AfterLogin;

import ClientBackend.UserOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Download {
    Socket socket;
    @FXML
    TextField keyField;
    @FXML
    public TextField pathField;
    @FXML
    public Label msgLabel;

    public void setSocket(Socket s) {
        this.socket=s;
    }

    public void browseBtnAction(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        File f = chooser.showDialog(new Stage());
        pathField.setText(f.getPath());
    }

    public void downloadBtnAction(ActionEvent actionEvent) throws IOException {
        String key = keyField.getText();
        String path = pathField.getText();
        File file = new File(path);
        if(!file.isDirectory()){
            msgLabel.setText("Enter a valid directory");
            return;
        }
        String msg = new UserOptions(socket).download(key,path);
        msgLabel.setText(msg);
    }
}

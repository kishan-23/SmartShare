package AfterLogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class SideBar {
    @FXML
    BorderPane mainPane;
    @FXML
    Button logoutBtn;
    @FXML
    BorderPane sidePane;
    Socket socket;

    public void setSocket(Socket s) {
        this.socket = s;
    }

    public void setMainPane(BorderPane pane){
        this.mainPane = pane;
    }

    public void uploadBtnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./upload.fxml"));
        AnchorPane pn = loader.load();
        Upload u = loader.getController();
        u.setSocket(socket);
        mainPane.setCenter(pn);
    }

    public void downloadBtnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./download.fxml"));
        AnchorPane pn = loader.load();
        Download d = loader.getController();
        d.setSocket(socket);
        mainPane.setCenter(pn);
    }

    public void historyBtnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./history.fxml"));
        AnchorPane pn = loader.load();
        History h = loader.getController();
        h.setSocket(socket);
        h.display();
        mainPane.setCenter(pn);
    }

    public void logoutBtnAction(ActionEvent actionEvent) {
        Stage st = (Stage) logoutBtn.getScene().getWindow();
        st.close();
    }

}

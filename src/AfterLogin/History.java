package AfterLogin;

import ClientBackend.UserOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ClientBackend.FileModal;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class History {
    @FXML
    TableView<FileModal> dataTable;
    @FXML
    TableColumn<FileModal,String> nameRow;
    @FXML
    TableColumn<FileModal,String> keyRow;
    @FXML
    TableColumn<FileModal,Integer> downloadsRow;
    @FXML
    TableColumn<FileModal,String> dateRow;
    @FXML
    TextField keyField;
    @FXML
    Button removeBtn;
    @FXML
    Label msgLabel;

    Socket socket;
    public void setSocket(Socket s) {
        this.socket = s;
    }

    public void display() throws IOException {
        ObservableList<FileModal> fileList = new UserOptions(socket).history();
        nameRow.setCellValueFactory(new PropertyValueFactory<>("name"));
        keyRow.setCellValueFactory(new PropertyValueFactory<>("key"));
        downloadsRow.setCellValueFactory(new PropertyValueFactory<>("downloadLeft"));
        dateRow.setCellValueFactory(new PropertyValueFactory<>("availableUpto"));
        System.out.println(fileList.size());
        dataTable.setItems(fileList);
    }

    public void removeBtnAction(ActionEvent actionEvent) throws IOException {
        String msg = new UserOptions(socket).delFile(keyField.getText());
        msgLabel.setText(msg);
    }
}

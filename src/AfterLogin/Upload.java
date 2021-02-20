package AfterLogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;

public class Upload {
    @FXML TextField pathField;
    @FXML Button browseBtn;
    @FXML DatePicker dateField;
    @FXML TextField maxDownloadField;
    @FXML TextArea commentArea;
    @FXML Label msgLabel;
    @FXML TextField nameField;

    public void browseBtnAction(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(new Stage());
        if(!file.exists())
        {
            msgLabel.setText("Enter a valid file");
        }
        nameField.setText(file.getName());
        pathField.setText(file.getPath());
    }

    public void uploadBtnAction(ActionEvent actionEvent) {
            File file = new File(pathField.getText());
            if(!file.exists()) try {
                throw new Exception();
            } catch (Exception e) {
                msgLabel.setText("Choose a valid file.");

        }
        //catch ()
        if(checkDate(dateField.getValue())&&checkMaxdownloads(maxDownloadField.getText()))
        {
            msgLabel.setText("UPLOAD was successful. Please note the following key:\nqw23#12/02/21#785@a78");
        }
        return;
    }

    boolean checkDate(LocalDate d) {
        LocalDate dt = LocalDate.now();
        if(dt.compareTo(d)>0) {
            msgLabel.setText("Enter a valid date of future.\n");
            return false;
        }

        return true;
    }

    boolean checkMaxdownloads(String str) {
        try{
            if(Integer.parseInt(str)>1000000) {
                msgLabel.setText("Enter a valid number less than 1000000 for maximun number of downloads.");
                return false;
            }
        }
        catch (Exception e)
        {
            msgLabel.setText("Enter a valid number less than 1000000 for maximun number of downloads.");
            return false;
        }
        return true;
    }
}

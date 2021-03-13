package ClientBackend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

public class FileModal {
    @FXML
    SimpleStringProperty name,key;
    SimpleIntegerProperty downloadLeft;
    SimpleStringProperty availableUpto;

    public FileModal(String name, String key, Integer downLeft, String avlUpto) {
        this.name = new SimpleStringProperty(name);
        this.key = new SimpleStringProperty(key);
        this.downloadLeft = new SimpleIntegerProperty(downLeft);
        this.availableUpto = new SimpleStringProperty(avlUpto);
    }

    public String getName() {
        return this.name.get();
    }

    public String getKey() {
        return this.key.get();
    }

    public String getAvailableUpto() {
        return this.availableUpto.get();
    }

    public Integer getDownloadLeft() {
        return this.downloadLeft.get();
    }
}

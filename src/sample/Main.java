package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load(); //FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Smart-Share");
        primaryStage.setScene(new Scene(root, 680, 450));
        primaryStage.setResizable(false);
        Controller controller = loader.getController();
        controller.client = new ClientBackend.Client();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

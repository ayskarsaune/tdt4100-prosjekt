package attendance;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AttendanceController {

    @FXML
    private Label result;

    @FXML
    private AnchorPane main;


    @FXML
    private void handleRegister(Event event) throws IOException {
        changeScene(event, "Register.fxml");

    }
    @FXML
    private void handleStats(Event event) throws IOException {
        changeScene(event, "Stats.fxml");
    }

    @FXML
    private void handleBack(Event event) throws IOException {
        changeScene(event, "App.fxml");
    }

    private void changeScene(Event event, String filepath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(filepath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.show();
    }

}

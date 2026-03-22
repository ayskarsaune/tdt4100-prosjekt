package attendance;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AttendanceController {

    @FXML
    private TextField firstNumber, secondNumber, operator;

    @FXML
    private Label result;

    @FXML
    private AnchorPane main;

    @FXML
    private void handleButtonClick() {



        /* initCalculator(operator.getText());
        try {
            int result = calculator.calculate(Integer.parseInt(firstNumber.getText()),
                    Integer.parseInt(secondNumber.getText()));
            this.result.setText(firstNumber.getText() + " " + operator.getText() + " " + secondNumber.getText() + " = "
                    + String.valueOf(result));
        } catch (NumberFormatException e) {
            result.setText("Et eller begge tallene er ugyldige");
        } */
    }

    @FXML
    private void handleRegister(Event event) throws IOException {
        Logic hey = Logic.getInstance();
        hey.setPerson(new Person("atre"));
        changeScene(event, "Register.fxml");

    }
    @FXML
    private void handleStats(Event event) throws IOException {
        changeScene(event, "Register.fxml");
    }

    @FXML
    private void handleBack(Event event) throws IOException {
        changeScene(event, "App.fxml");
    }

    private void changeScene(Event event, String filepath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(filepath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        
        stage.show();
    }

}

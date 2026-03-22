package attendance;

import java.io.IOException;
import java.util.List;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField firstNumber, secondNumber, operator;

    @FXML
    private Label result;

    @FXML
    private VBox names;

    @FXML
    private HBox subjectNames;

    @FXML
    private GridPane main;

    @FXML
    private void handleBack(Event event) throws IOException {
        changeScene(event, "App.fxml");
    }

    @FXML
    public void initialize() {
        Logic info = Logic.getInstance();
        List<Person> people = info.getPeople();
        if (people.isEmpty()) {
            //TODO: Add "The file is currently empty, please start adding people and subjects or import from file"
        }
        GridPane grid = new GridPane(5,5);
        
        for (Person person : people) {
            Label name = new Label(person.getName());
            name.setPrefHeight(30);
            
            names.getChildren().add(name);
            

            ToggleButton button = new ToggleButton("Ikke møtt");
            button.setId(person.getName());
            button.setPrefWidth(70);
            button.setOnAction(e -> { if (button.isSelected()) {
                                        button.setText("Til stede");
                                        person.addSubject(new Subject("Matte", "TMA4422", List.of(2,4), info.getSemester()));
                                    } else {
                                        button.setText("Ikke møtt");
                                    }
                                
                                });
            grid.addRow(people.indexOf(person),button);
        }
        List<Subject> subjects = info.getSubjects(info.getSemester());
        for (Subject subject : subjects) {
            subjectNames.getChildren().add(new Label(subject.getCode()));
        }

        main.add(grid,1,1);
        
        
    }


    private void changeScene(Event event, String filepath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(filepath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

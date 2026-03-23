package attendance;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;

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
        PersonContainer info = PersonContainer.getInstance();
        List<Person> people = info.getPeople();
        if (people.isEmpty()) {
            //TODO: Add "The file is currently empty, please start adding people and subjects or import from file"
        }
        GridPane grid = new GridPane(5,5);

        for (Person person : people) {
                Label name = new Label(person.getName());
                name.setPrefHeight(30);
                
                names.getChildren().add(name);
        }
        
        
        List<Lecture> lectures = info.getWeekLectures(7,info.getSemester());
        for (Lecture lecture : lectures) {
            Label label = new Label(lecture.getSubject().getCode());
            label.setPrefWidth(70);
            subjectNames.getChildren().add(label);

            for (Person person : people) {
                if (person.getSubjects().contains(lecture.getSubject())) {
                ToggleButton button = new ToggleButton();
                if (!person.getAttendedLectures().contains(lecture)) {
                    button.setText("Ikke møtt");
                } else {
                    button.setText("Til stede");
                    button.setSelected(true);
                }
                button.setPrefWidth(70);
                button.setOnAction(e -> { if (button.isSelected()) {
                                            button.setText("Til stede");
                                            person.addLecture(lecture);
                                        } else {
                                            button.setText("Ikke møtt");
                                            person.removeLecture(lecture);
                                        }
                                
                                });
            grid.add(button, lectures.indexOf(lecture),people.indexOf(person));
            
        }}
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

    public void writeToFile(String filePath) {
        File file = new File(filePath);
        try {
            CSVWriter writer = new CSVWriter(file);
        }
        
    }
}

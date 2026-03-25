package attendance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Date;
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
    private PersonContainer info = PersonContainer.getInstance();
    private int currentWeek = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

    @FXML
    private TextField firstNumber, secondNumber, operator;

    @FXML
    private Label result, weekDisplay;

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

    public RegisterController() {
       
    }

    @FXML
    public void initialize() {
        setWeek(currentWeek);
    }

    private void setWeek(int week) {
        List<Person> people = info.getPeople();
        weekDisplay.setText("Uke "+week);

        if (people.isEmpty()) {
            //TODO: Add "The file is currently empty, please start adding people and subjects or import from file"
        }
        GridPane grid = new GridPane(5,5);
        names.getChildren().clear();
        for (Person person : people) {
                Label name = new Label(person.getName());
                name.setPrefHeight(30);
                
                names.getChildren().add(name);
        }
        
        
        List<Lecture> lectures = info.getWeekLectures(week,info.getSemester());
        subjectNames.getChildren().clear();
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

    @FXML
    private void handleSave(Event event) {
        info.writeToFile("register.csv");
    }
    @FXML
    private void handleLoad(Event event) {
        info.readFromFile("register.csv");
        System.out.println(this.currentWeek);
        setWeek(this.currentWeek);
    }

    @FXML
    private void forward() {
        this.currentWeek++;
        setWeek(this.currentWeek);
    }
    
    @FXML
    private void backward() {
        this.currentWeek--;
        setWeek(this.currentWeek);
    }

    
}

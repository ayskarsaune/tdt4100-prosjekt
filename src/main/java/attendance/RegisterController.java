package attendance;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterController {
    private PersonContainer container = PersonContainer.getInstance();
    private int currentWeek = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    private int currentYear = LocalDate.now().getYear();

    @FXML
    private Label result, weekDisplay, empty;

    @FXML
    private VBox names;

    @FXML
    private HBox subjectNames, dates;

    @FXML
    private GridPane main;

    @FXML
    private TextField semesterName;

    @FXML
    private ChoiceBox<Subject> lectureSubject;

    @FXML
    private DatePicker lectureDate;


    @FXML
    private void handleBack(Event event) throws IOException {
        changeScene(event, "App.fxml");
    }

    @FXML
    private void handleEdit(Event event) throws IOException {
        changeScene(event, "Edit.fxml");
    }

    public RegisterController() {
       
    }

    @FXML
    public void initialize() {
        setWeek(currentWeek, this.currentYear);
    }

    private void setWeek(int week, int year) {
        List<Person> people = container.getPeople();
        weekDisplay.setText("Uke "+week);

        if (people.isEmpty()) {
            empty.setText("File is currently empty, start adding people and subjects or load from file");
        } else {
            empty.setText(null);
        }

        if (main.getChildren().size() > 3) {
            main.getChildren().remove(3);
        }

        GridPane grid = new GridPane(5,5);
        names.getChildren().clear();
        for (Person person : people) {
                Label name = new Label(person.getName());
                name.setPrefHeight(30);
                
                names.getChildren().add(name);
        }
        
        
        List<Lecture> lectures = container.getWeekLectures(week, year);
        subjectNames.getChildren().clear();
        dates.getChildren().clear();
        for (Lecture lecture : lectures) {
            Label label = new Label(lecture.getSubject().getCode());
            label.setPrefWidth(120);
            label.setAlignment(Pos.CENTER);
            subjectNames.getChildren().add(label);

            Label date = new Label(lecture.getDate().toString().substring(5));
            date.setPrefWidth(120);
            date.setAlignment(Pos.CENTER);
            dates.getChildren().add(date);

            for (Person person : people) {
                if (person.getSubjects().contains(lecture.getSubject())) {
                    ToggleButton button = new ToggleButton();
                    if (!person.getAttendedLectures().contains(lecture)) {
                        button.setText("Ikke møtt");
                    } else {
                        button.setText("Til stede");
                        button.setSelected(true);
                    }
                    button.setPrefWidth(120);
                    button.setOnAction(e -> { if (button.isSelected()) {
                                                button.setText("Til stede");
                                                person.addLecture(lecture);
                                            } else {
                                                button.setText("Ikke møtt");
                                                person.removeLecture(lecture);
                                            }
                                    
                                    });
            grid.add(button, lectures.indexOf(lecture),people.indexOf(person));
        } else {
            Label filler = new Label("");
            filler.setPrefHeight(26);
            grid.add(filler, lectures.indexOf(lecture),people.indexOf(person));
        }
    }
        Button delete = new Button("Slett");
        delete.setPrefWidth(120);
        delete.setOnAction(e -> {
            lecture.getSubject().removeLecture(lecture);
            setWeek(currentWeek, currentYear);
        });
        grid.add(delete, lectures.indexOf(lecture), people.size());
        } 
        main.add(grid,1,1);
        ObservableList<Subject> observableList = FXCollections.observableArrayList(container.getSubjects().stream().filter(s -> s.getSemester().contains(LocalDate.of(currentYear,1,1).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,(currentWeek)))).toList());
        lectureSubject.setItems(observableList);
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
        container.writeToFile("register.csv");
    }
    @FXML
    private void handleLoad(Event event) {
        container.readFromFile("register.csv");
        System.out.println(this.currentWeek);
        setWeek(this.currentWeek, this.currentYear);
    }

    @FXML
    private void forward() {
        this.currentWeek++;
        if (this.currentWeek == 53) {
            this.currentWeek = 1;
            this.currentYear++;
        }
        setWeek(this.currentWeek, this.currentYear);
    }
    
    @FXML
    private void backward() {
        this.currentWeek--;
        if (this.currentWeek == 0) {
            this.currentWeek = 52;
            this.currentYear--;
        }
        setWeek(this.currentWeek, this.currentYear);
    }

    @FXML
    private void addLecture() {
        if (lectureSubject.getValue() != null && lectureSubject.getValue().getSemester().contains(lectureDate.getValue())) {
            lectureSubject.getValue().addLecture(lectureDate.getValue());
            lectureSubject.setValue(null);
            setWeek(currentWeek, currentYear);
        }
        
    }

    
}

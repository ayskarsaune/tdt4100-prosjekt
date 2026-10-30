package attendance;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StatsController {
        private PersonContainer container = PersonContainer.getInstance();

    @FXML 
    public GridPane statGrid;

    @FXML 
    public MenuButton subjectSelector;

    @FXML 
    public ChoiceBox<Semester> semesterSelector;
    
    @FXML
    private void initialize() {
        for (Subject subject : container.getSubjects()) {
            CheckBox checkName = new CheckBox(subject.getCode());
            CustomMenuItem custom = new CustomMenuItem(checkName, false);
            subjectSelector.getItems().add(custom);
        }

        ObservableList<Semester> observableList = FXCollections.observableArrayList(container.getSemesters());
        semesterSelector.setItems(observableList);
        
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @FXML 
    private void updateValues() {
        List<Person> people = container.getPeople();
        statGrid.getChildren().subList(5, statGrid.getChildren().size()).clear();
        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            statGrid.add(new Label(person.getName()), 0, i+1);
            statGrid.add(new Label(Double.toString(round((((double) person.getAttendedLectures().size()) / (double)person.getTotalLectures().stream().filter(l -> l.getDate().isBefore(LocalDate.now().plusDays(1))).toList().size())*100,1))+"%"), 1, i+1);
            if (semesterSelector.getValue() != null) {
                Semester semester = semesterSelector.getValue();
                statGrid.add(new Label(Double.toString(round((((double) person.getAttendedLectures(semester).size()) / (double)person.getTotalLectures(semester).stream().filter(l -> l.getDate().isBefore(LocalDate.now().plusDays(1))).toList().size())*100,1))+"%"), 2, i+1);
            }
        } 

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

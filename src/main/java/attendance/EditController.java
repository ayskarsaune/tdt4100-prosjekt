package attendance;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class EditController {
    private PersonContainer container = PersonContainer.getInstance();

    @FXML
    private AnchorPane lectures;

    @FXML
    private GridPane semesters,subjects, people;

    @FXML
    private TabPane categories;

    @FXML
    private DatePicker semesterstart, semesterEnd;

    @FXML
    private TextField semesterName, subjectCode, subjectName, personName;

    @FXML
    private ChoiceBox<Semester> semesterSelector;

    @FXML
    private MenuButton personSelector, subjectSelector;

    @FXML
    private HBox days;


    @FXML
    private void handleBack(Event event) throws IOException {
        changeScene(event, "Register.fxml");

    }
    

    @FXML
    private void initialize() {
        showSemesters();
        semesterstart.setEditable(false);
        semesterEnd.setEditable(false);
        
        showSubjects();
        showPeople();
        
    }

    private void showSemesters() {
        List<Semester> semesterList = container.getSemesters();
        semesters.getChildren().subList(5, semesters.getChildren().size()).clear();
        for (int i = 0; i < semesterList.size();i++) {
            Semester semester = semesterList.get(i);
            semesters.add(new Label(semester.getName()),0,i+1);
            semesters.add(new Label(Integer.toString(semester.getStartWeek())),1,i+1);
            semesters.add(new Label(Integer.toString(semester.getEndWeek())),2,i+1);
            semesters.add(new Label(Integer.toString(semester.getYear())),3,i+1);

            Button button = new Button("Delete");
            button.setOnAction((e) -> {container.removeSemester(semester);
                showSemesters();
            });
            semesters.add(button,5,i+1);
        }
        

        ObservableList<Semester> observableList = FXCollections.observableArrayList(container.getSemesters());
        semesterSelector.setItems(observableList);
    }

    private void showSubjects() {
        List<Subject> subjectList = container.getSubjects();
        subjects.getChildren().subList(3, subjects.getChildren().size()).clear();
        for (int i = 0; i<subjectList.size();i++) {
            Subject subject = subjectList.get(i);
            subjects.add(new Label(subject.getCode()),0,i+1);
            subjects.add(new Label(subject.getName()),1,i+1);
            subjects.add(new Label(subject.getSemester().getName()),2,i+1);
            Button button = new Button("Slett fag");
            button.setOnAction((e) -> container.removeSubject(subject));
            subjects.add(button,4,i+1);
        }

        for (Subject subject : container.getSubjects()) {
            CheckBox checkName = new CheckBox(subject.getCode());
            CustomMenuItem custom = new CustomMenuItem(checkName, false);
            subjectSelector.getItems().add(custom);
        }
    }

    private void showPeople() {
        personSelector.getItems().clear();
        for (Person person : container.getPeople()) {
            CheckBox checkName = new CheckBox(person.getName());
            CustomMenuItem custom = new CustomMenuItem(checkName, false);
            personSelector.getItems().add(custom);
        }


        List<Person> personList = container.getPeople();
        people.getChildren().subList(2, people.getChildren().size()).clear();
        for (int i = 0; i<personList.size();i++) {
            Person person = personList.get(i);
            people.add(new Label(person.getName()),0,i+1);
            people.add(new Label(person.getSubjects().stream().map(s -> s.getName()).collect(Collectors.joining(", "))),1,i+1);
            
            Button button = new Button("Slett person");
            button.setOnAction((e) -> container.removePerson(person));
            people.add(button,3,i+1);
            
        }
    }

    @FXML
    private void setStart() {
            semesterEnd.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                Date dateIn = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date startDate = Date.from(semesterstart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                setDisable(empty || dateIn.before(startDate));
            }
        });
    }
    @FXML
    private void setEnd() {
            semesterstart.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                Date dateIn = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date endDate = Date.from(semesterEnd.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                setDisable(empty || dateIn.after(endDate));
            }
        });
    }

    @FXML
    private void setSemester() {
       Semester semester = new Semester(semesterstart.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)+1, semesterEnd.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR),semesterstart.getValue().getYear(), semesterName.getText());
       container.addSemester(semester);
       showSemesters();
    }

    @FXML
    private void addSubject() {
        List<Integer> daysOfWeek = new ArrayList<>();
        for (Node node : days.getChildren()) {
            if (node.getAccessibleRole().equals(AccessibleRole.TOGGLE_BUTTON)) {
                if (((ToggleButton) node).isSelected()) {
                    System.out.println(days.getChildren().indexOf(node)+1);
                    daysOfWeek.add(days.getChildren().indexOf(node)+1);
                    ((ToggleButton) node).setSelected(false);
                }
            }
        }

        Subject subject = new Subject(subjectName.getText(), subjectCode.getText(), daysOfWeek, semesterSelector.getValue());
        container.addSubject(subject);

        subjectName.setText(null);
        subjectCode.setText(null);

        for (MenuItem checkbox : personSelector.getItems()) {
            if (checkbox instanceof CustomMenuItem) {
                CheckBox person = (CheckBox)((CustomMenuItem)checkbox).getContent();
                if (person.isSelected()) {
                    Person selected = container.getPeople().stream().filter(p -> p.getName().equals(person.getText())).findAny().orElseThrow();
                    selected.addSubject(subject);
                }
            }
        }

        showSubjects();
    }

    

    @FXML
    private void addPerson() {
        Person person = new Person(personName.getText());
        container.addPerson(person);
        for (MenuItem checkbox : subjectSelector.getItems()) {
            if (checkbox instanceof CustomMenuItem) {
                CheckBox subject = (CheckBox)((CustomMenuItem)checkbox).getContent();
                if (subject.isSelected()) {
                    person.addSubject(container.getSubjects().stream().filter((s) -> s.getCode().equals(subject.getText())).findAny().orElseThrow(IllegalStateException::new));
                }
            }
        }
    }

    private void changeScene(Event event, String filepath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(filepath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.show();
    }

}

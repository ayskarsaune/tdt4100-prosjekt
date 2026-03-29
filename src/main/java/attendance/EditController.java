package attendance;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditController {
    private PersonContainer container = PersonContainer.getInstance();

    @FXML
    private AnchorPane subjects, lectures, people;

    @FXML
    private GridPane semesters;

    @FXML
    private TabPane categories;

    @FXML
    private DatePicker semesterstart, semesterEnd;

    @FXML
    private TextField semesterName;


    @FXML
    private void handleBack(Event event) throws IOException {
        changeScene(event, "Register.fxml");

    }
    

    @FXML
    private void initialize() {
        List<Semester> semesterList = container.getSemesters();
        for (int i = 0; i < semesterList.size();i++) {
            Semester semester = semesterList.get(i);
            semesters.add(new Label(semester.getName()),0,i+1);
            semesters.add(new Label(Integer.toString(semester.getStartWeek())),1,i+1);
            semesters.add(new Label(Integer.toString(semester.getEndWeek())),2,i+1);
            semesters.add(new Label(Integer.toString(semester.getYear())),3,i+1);

            Button button = new Button("Delete");
            button.setOnAction((e) -> {container.removeSemester(semester);
                                       System.out.println("hey");
            });
            semesters.add(button,5,i+1);
        }
        semesterstart.setEditable(false);
        semesterEnd.setEditable(false);
        
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
       Semester semester = new Semester(semesterstart.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR), semesterEnd.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR),semesterstart.getValue().getYear(), semesterName.getText());
       container.addSemester(semester);
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

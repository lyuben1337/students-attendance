package com.example.studentattendance.gui.controllers.schedule;

import com.example.studentattendance.database.models.entity.Classroom;
import com.example.studentattendance.database.models.entity.Course;
import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.models.entity.Schedule;
import com.example.studentattendance.database.service.ClassroomService;
import com.example.studentattendance.database.service.CourseService;
import com.example.studentattendance.database.service.ScheduleService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Controller class for managing schedule adding in the GUI.
 */
@Component
public class NewScheduleController {
    @FXML
    private ComboBox<Course> courseComboBox;

    @FXML
    private ComboBox<Classroom> classroomComboBox;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<String> startTimeField;

    @FXML
    private TextField endTimeField;

    private Group selectedGroup;

    private static final List<String> SCHEDULE_START_TIME_LIST =
            List.of("07:45", "09:30", "11:15", "13:10", "14:55", "16:40", "18:25");

    private static final int SCHEDULE_DURATION = 95; // minutes

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private CourseService courseService;

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void initialize() {
        courseComboBox.setItems(FXCollections.observableArrayList(courseService.getAllCourses()));
        classroomComboBox.setItems(FXCollections.observableArrayList(classroomService.getAllClassrooms()));
        startTimeField.setItems(FXCollections.observableArrayList(SCHEDULE_START_TIME_LIST));

        dateField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        startTimeField.setOnAction(event ->
                endTimeField.setText(LocalTime.parse(
                        startTimeField.getSelectionModel().getSelectedItem()
                    ).plusMinutes(SCHEDULE_DURATION).toString()
                )
        );
    }

    public void handleAddButton(ActionEvent actionEvent) {
        try {
            if (classroomComboBox.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Аудиторія має бути вказана");
            }
            if (courseComboBox.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Предмет має бути вказаний");
            }
            if (dateField.getEditor().getText().isBlank()) {
                throw new Exception("Дата має бути вказана");
            }
            if (startTimeField.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Час початку має бути вказан");
            }

            var date = Date.from(
                    dateField.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

            var startTime = LocalTime.parse(startTimeField.getSelectionModel().getSelectedItem());
            var endTime = startTime.plusMinutes(SCHEDULE_DURATION);

            scheduleService.addSchedule(Schedule.builder()
                    .course(courseComboBox.getSelectionModel().getSelectedItem())
                    .group(this.selectedGroup)
                    .classroom(classroomComboBox.getSelectionModel().getSelectedItem())
                    .date(date)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build());

            Stage stage = (Stage) classroomComboBox.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            new Alert(ERROR, e.getMessage()).showAndWait();
        }
    }
}

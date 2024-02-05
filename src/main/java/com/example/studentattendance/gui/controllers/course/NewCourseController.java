package com.example.studentattendance.gui.controllers.course;

import com.example.studentattendance.database.models.entity.Course;
import com.example.studentattendance.database.service.CourseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller class for managing course adding in the GUI.
 */
@Component
public class NewCourseController {
    @FXML
    private TextField courseCodeField;

    @FXML
    private TextField courseNameField;

    @Autowired
    private CourseService courseService;

    public void handleAddButton() {
        try {
            if (courseCodeField.getText().isBlank()) {
                throw new Exception("Код курсу має бути вказана");
            }
            if (courseNameField.getText().isBlank()) {
                throw new Exception("Повна назва курсу має бути вказана");
            }
            courseService.addCourse(Course.builder()
                    .name(courseNameField.getText())
                    .code(courseCodeField.getText())
                    .build());
            Stage stage = (Stage) courseNameField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }
}

package com.example.studentattendance.gui.controllers.classroom;

import com.example.studentattendance.database.models.entity.Classroom;
import com.example.studentattendance.database.service.ClassroomService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller class for managing adding of classroom in the GUI.
 */
@Component
public class NewClassroomController {
    @FXML
    private  TextField capacityField;

    @FXML
    private TextField numberField;

    @Autowired
    private ClassroomService classroomService;

    public void handleAddButton() {
        try {
            if (capacityField.getText().isBlank()) {
                throw new Exception("Вмістимість аудиторії має бути вказана");
            }
            if (numberField.getText().isBlank()) {
                throw new Exception("Номер аудиторії має бути вказана");
            }
            classroomService.addClassroom(Classroom.builder()
                    .capacity(Integer.parseInt(capacityField.getText()))
                    .number(numberField.getText())
                    .build());
            Stage stage = (Stage) numberField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }
}

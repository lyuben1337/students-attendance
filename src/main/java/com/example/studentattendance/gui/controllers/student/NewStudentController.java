package com.example.studentattendance.gui.controllers.student;

import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.models.entity.Student;
import com.example.studentattendance.database.service.GroupService;
import com.example.studentattendance.database.service.StudentService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Controller class for managing student adding in the GUI.
 */
@Component
public class NewStudentController {
    @FXML
    private TextField lastNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private DatePicker birthDateField;

    @FXML
    private ComboBox<Group> groupComboBox;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    @FXML
    private void initialize() {
        groupComboBox.setItems(FXCollections.observableArrayList(groupService.getAllGroups()));
    }

    @FXML
    private void handleAddButton(ActionEvent actionEvent) {
        try {
            if (firstNameField.getText().isBlank()) {
                throw new Exception("Імʼя має бути вказано");
            }
            if (middleNameField.getText().isBlank()) {
                throw new Exception("По батькові має бути вказано");
            }
            if (lastNameField.getText().isBlank()) {
                throw new Exception("Прізвище має бути вказано");
            }
            if (birthDateField.getEditor().getText().isBlank()) {
                throw new Exception("Дата народженя має бути вказаною");
            }
            if (groupComboBox.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Треба обрати групу");
            }
            if (emailField.getText().isBlank()) {
                throw new Exception("Email має бути вказаний");
            }
            if (phoneField.getText().isBlank()) {
                throw new Exception("Номер телфону має бути вказаний");
            }
            var birthDate = Date.from(
                    birthDateField.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

            studentService.saveOrUpdateStudent(
                    Student.builder()
                            .email(emailField.getText())
                            .group(groupComboBox.getSelectionModel().getSelectedItem())
                            .phoneNumber(phoneField.getText())
                            .firstName(firstNameField.getText())
                            .middleName(middleNameField.getText())
                            .lastName(lastNameField.getText())
                            .birthDate(birthDate)
                            .build());
            Stage stage = (Stage) lastNameField.getScene().getWindow();
            stage.close();
        }
        catch (Exception e) {
            new Alert(ERROR, e.getMessage()).showAndWait();
        }
    }
}

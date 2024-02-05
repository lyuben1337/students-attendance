package com.example.studentattendance.gui.controllers.student;

import com.example.studentattendance.database.models.entity.Group;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Controller class for managing student editing in the GUI.
 */
@Component
public class EditStudentController {
    @FXML
    private TextField emailField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private DatePicker birthDateField;

    @FXML
    private ComboBox<Group> groupComboBox;

    @FXML
    private TextField phoneField;

    private Long editedStudentId;

    @Autowired
    private GroupService groupService;

    @Autowired
    private StudentService studentService;

    public void setEditedField(Long id) {
        this.editedStudentId = id;
        try {
            var editedStudent = studentService.getStudent(editedStudentId);
            firstNameField.setText(editedStudent.getFirstName());
            middleNameField.setText(editedStudent.getMiddleName());
            lastNameField.setText(editedStudent.getLastName());
            birthDateField.setValue(LocalDate.ofInstant(editedStudent.getBirthDate().toInstant(),
                    ZoneId.systemDefault())
            );
            groupComboBox.setItems(FXCollections.observableArrayList(groupService.getAllGroups()));
            groupComboBox.getSelectionModel().select(editedStudent.getGroup());
            phoneField.setText(editedStudent.getPhoneNumber());
            emailField.setText(editedStudent.getEmail());
        } catch (Exception e) {
            new Alert(ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent actionEvent) {
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
                    birthDateField.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
            );

            var student = studentService.getStudent(editedStudentId);
            student.setFirstName(firstNameField.getText());
            student.setMiddleName(middleNameField.getText());
            student.setLastName(lastNameField.getText());
            student.setEmail(emailField.getText());
            student.setBirthDate(birthDate);
            student.setPhoneNumber(phoneField.getText());
            student.setGroup(groupComboBox.getValue());
            studentService.saveOrUpdateStudent(student);

            var stage = (Stage) groupComboBox.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            new Alert(ERROR, e.getMessage()).showAndWait();
        }
    }
}

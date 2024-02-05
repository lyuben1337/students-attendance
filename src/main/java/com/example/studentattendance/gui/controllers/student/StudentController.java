package com.example.studentattendance.gui.controllers.student;

import com.example.studentattendance.database.models.dto.StudentDTO;
import com.example.studentattendance.database.service.StudentService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller class for managing student-related operations in the GUI.
 */
@Component
public class StudentController {
    @FXML
    private TableView<StudentDTO> tableView;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ApplicationContext context;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button refreshButton;

    public void initialize() {
        updateData();

        addButton.setOnAction(event -> handleAddButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
        editButton.setOnAction(event -> handleEditButton());
        refreshButton.setOnAction(event -> updateData());
    }

    private void handleEditButton() {
        var selectedStudent = tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/student/edit_student.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            EditStudentController controller = loader.getController();
            controller.setEditedField(selectedStudent.getId());

            Stage editStudentStage = new Stage();
            editStudentStage.initStyle(StageStyle.UTILITY);
            editStudentStage.initModality(Modality.APPLICATION_MODAL);
            editStudentStage.setTitle("Змінити студента");

            editStudentStage.setScene(new Scene(root));

            editStudentStage.setResizable(false);

            editStudentStage.showAndWait();
            updateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/student/new_student.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            Stage addStudentStage = new Stage();
            addStudentStage.initStyle(StageStyle.UTILITY);
            addStudentStage.initModality(Modality.APPLICATION_MODAL);
            addStudentStage.setTitle("Додавання студента");

            addStudentStage.setScene(new Scene(root));

            addStudentStage.setResizable(false);

            addStudentStage.showAndWait();
            updateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteButton() {
        StudentDTO selectedStudent = tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Підтвердження");
        alert.setHeaderText("Видалити студента");
        alert.setContentText(String.format("Ви впевнені, що хочете видалити студента %s?", selectedStudent.getName()));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentService.deleteStudent(selectedStudent.getId());
            updateData();
        }
    }

    public void updateData() {
        tableView.setItems(FXCollections.observableArrayList(studentService.getAllStudents()));
    }
}

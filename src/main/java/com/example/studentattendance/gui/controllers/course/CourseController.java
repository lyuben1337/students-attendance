package com.example.studentattendance.gui.controllers.course;

import com.example.studentattendance.database.models.entity.Course;
import com.example.studentattendance.database.service.CourseService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller class for managing course-related operations in the GUI.
 */
@Component
public class CourseController {
    @FXML
    private TableColumn<Course, String> nameColumn;

    @FXML
    private TableColumn<Course, String> codeColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<Course> tableView;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ApplicationContext context;

    public void initialize() {
        updateData();

        addButton.setOnAction(event -> handleAddButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
        refreshButton.setOnAction(event -> updateData());

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
            courseService.addCourse(event.getRowValue());
        });

        codeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        codeColumn.setOnEditCommit(event -> {
            event.getRowValue().setCode(event.getNewValue());
            courseService.addCourse(event.getRowValue());
        });
    }

    private void updateData() {
        tableView.setItems(FXCollections.observableArrayList(courseService.getAllCourses()));
    }

    private void handleDeleteButton() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Підтвердження");
        alert.setHeaderText("Видалити групу");
        alert.setContentText(String.format("Ви впевнені, що хочете видалити предмет %s? " +
                "В роскладі цей предмет видалиться автоматично!", selectedCourse.getName()));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            courseService.deleteCourse(selectedCourse);
            updateData();
        }
    }

    private void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/course/new_course.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            Stage addStudentStage = new Stage();
            addStudentStage.initStyle(StageStyle.UTILITY);
            addStudentStage.initModality(Modality.APPLICATION_MODAL);
            addStudentStage.setTitle("Додавання предмету");

            addStudentStage.setScene(new Scene(root));

            addStudentStage.setResizable(false);

            addStudentStage.showAndWait();
            updateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

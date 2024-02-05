package com.example.studentattendance.gui.controllers.classroom;

import com.example.studentattendance.database.models.entity.Classroom;
import com.example.studentattendance.database.service.ClassroomService;
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
import javafx.util.converter.IntegerStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Controller class for managing classroom-related operations in the GUI.
 */
@Component
public class ClassroomController {
    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<Classroom> tableView;

    @FXML
    private TableColumn<Classroom, String> numberColumn;

    @FXML
    private TableColumn<Classroom, Integer> capacityColumn;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ApplicationContext context;

    public void initialize() {
        updateData();

        addButton.setOnAction(event -> handleAddButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
        refreshButton.setOnAction(event -> updateData());

        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        numberColumn.setOnEditCommit(event -> {
            event.getRowValue().setNumber(event.getNewValue());
            classroomService.updateClassroom(event.getRowValue());
        });

        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        capacityColumn.setOnEditCommit(event -> {
            try {
                event.getRowValue().setCapacity(event.getNewValue());
                classroomService.updateClassroom(event.getRowValue());
            } catch (Exception e) {
                new Alert(ERROR, e.getMessage()).showAndWait();
            }
        });
    }

    private void updateData() {
        tableView.setItems(FXCollections.observableArrayList(classroomService.getAllClassrooms()));
    }

    private void handleDeleteButton() {
        Classroom selectedClassroom = tableView.getSelectionModel().getSelectedItem();
        if (selectedClassroom == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Підтвердження");
        alert.setHeaderText("Видалити аудиторію");
        alert.setContentText(String.format("Ви впевнені, що хочете видалити аудиторію %s? " +
                "В роскладі ця аудиторія видалиться автоматично!", selectedClassroom.getNumber()));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            classroomService.deleteClassroom(selectedClassroom);
            updateData();
        }
    }

    private void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/classroom/new_classroom.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            Stage addStudentStage = new Stage();
            addStudentStage.initStyle(StageStyle.UTILITY);
            addStudentStage.initModality(Modality.APPLICATION_MODAL);
            addStudentStage.setTitle("Додавання аудиторії");

            addStudentStage.setScene(new Scene(root));

            addStudentStage.setResizable(false);

            addStudentStage.showAndWait();
            updateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

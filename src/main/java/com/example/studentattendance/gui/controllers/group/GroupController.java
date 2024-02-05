package com.example.studentattendance.gui.controllers.group;

import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.service.GroupService;
import com.example.studentattendance.gui.controllers.MainController;
import com.example.studentattendance.gui.controllers.schedule.GroupScheduleController;
import com.example.studentattendance.gui.controllers.student.StudentsOfGroupController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
 * Controller class for managing group-related operations in the GUI.
 */
@Component
public class GroupController {
    @FXML
    private Button refreshButton;

    @FXML
    private Button getScheduleButton;

    @FXML
    private Button getStudentsButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Group> tableView;

    @FXML
    private TableColumn<Group, String> nameColumn;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ApplicationContext context;

    public void initialize() {
        updateData();

        addButton.setOnAction(event -> handleAddButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
        refreshButton.setOnAction(event -> updateData());
        getStudentsButton.setOnAction(event -> handleGetStudentsButton());
        getScheduleButton.setOnAction(event -> handleGetScheduleButton());

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setEditable(true);
        nameColumn.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
            groupService.addGroup(event.getRowValue());
        });
    }

    private void handleGetScheduleButton() {
        Group selectedGroup = tableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schedule/group_schedule.fxml"));
            loader.setControllerFactory(context::getBean);
            Node content = loader.load();

            GroupScheduleController studentsOfGroupController = loader.getController();
            studentsOfGroupController.setSelectedGroup(selectedGroup);
            studentsOfGroupController.initialize();

            Tab newTab = new Tab(String.format("Розклад: %s", selectedGroup.getName()));
            newTab.setContent(content);

            MainController mainController = context.getBean(MainController.class);
            mainController.addTab(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGetStudentsButton() {
        Group selectedGroup = tableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/student/students_of_group.fxml"));
            loader.setControllerFactory(context::getBean);
            Node content = loader.load();

            StudentsOfGroupController studentsOfGroupController = loader.getController();
            studentsOfGroupController.setGroup(selectedGroup);
            studentsOfGroupController.initialize();

            Tab newTab = new Tab(String.format("Студенти: %s", selectedGroup.getName()));
            newTab.setContent(content);

            MainController mainController = context.getBean(MainController.class);
            mainController.addTab(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteButton() {
        Group selectedGroup = tableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Підтвердження");
        alert.setHeaderText("Видалити групу");
        alert.setContentText(String.format("Ви впевнені, що хочете видалити групу %s? " +
                "Студенти та розклад цієї групи видаляться автоматично!", selectedGroup.getName()));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            groupService.deleteGroup(selectedGroup);
            updateData();
        }
    }

    private void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/group/new_group.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            Stage addStudentStage = new Stage();
            addStudentStage.initStyle(StageStyle.UTILITY);
            addStudentStage.initModality(Modality.APPLICATION_MODAL);
            addStudentStage.setTitle("Додавання групи");

            addStudentStage.setScene(new Scene(root));

            addStudentStage.setResizable(false);

            addStudentStage.showAndWait();
            updateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        tableView.setItems(FXCollections.observableArrayList(groupService.getAllGroups()));
    }
}

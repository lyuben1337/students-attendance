package com.example.studentattendance.gui.controllers.schedule;

import com.example.studentattendance.database.models.dto.ScheduleDTO;
import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.service.ScheduleService;
import com.example.studentattendance.gui.controllers.MainController;
import com.example.studentattendance.gui.controllers.attendance.AttendanceController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Controller class for managing schedule-related operations in the GUI.
 */
@Component
public class GroupScheduleController {
    @FXML
    private Button getAttendanceButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;

    @FXML
    private TableView<ScheduleDTO> tableView;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ApplicationContext context;

    private Group selectedGroup;

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void initialize() {
        updateData();

        addButton.setOnAction(event -> handleAddButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
        getAttendanceButton.setOnAction(event -> handleGetAttendanceButton());
        refreshButton.setOnAction(event -> updateData());
    }

    private void handleGetAttendanceButton() {
        ScheduleDTO selectedSchedule = tableView.getSelectionModel().getSelectedItem();
        if (selectedSchedule == null) return;
        try {
            var schedule = scheduleService.getSchedule(selectedSchedule.getId());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/attendance/attendance.fxml"));
            loader.setControllerFactory(context::getBean);
            Node content = loader.load();

            AttendanceController attendanceController = loader.getController();
            attendanceController.setSelectedScheduleId(selectedSchedule.getId());
            attendanceController.updateData();

            Tab newTab = new Tab(String.format("%s %s %s:",
                    schedule.getGroup().getName(),
                    schedule.getCourse().getCode(),
                    new SimpleDateFormat("dd.MM").format(schedule.getDate()))
            );
            newTab.setContent(content);

            MainController mainController = context.getBean(MainController.class);
            mainController.addTab(newTab);
        } catch (Exception e) {
            new Alert(ERROR, e.getMessage()).showAndWait();
        }
    }

    private void handleDeleteButton() {
        ScheduleDTO selectedSchedule = tableView.getSelectionModel().getSelectedItem();
        if (selectedSchedule == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Підтвердження");
        alert.setHeaderText("Видалити з роскладу");
        alert.setContentText(String.format("Ви впевнені, що хочете видалити %s %s з розкладу для групи %s?",
                selectedSchedule.getCourse(), selectedSchedule.getDate(), selectedGroup.getName()));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            scheduleService.deleteSchedule(selectedSchedule.getId());
            updateData();
        }
    }

    private void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schedule/new_schedule.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            Stage addScheduleStage = new Stage();
            addScheduleStage.initStyle(StageStyle.UTILITY);
            addScheduleStage.initModality(Modality.APPLICATION_MODAL);
            addScheduleStage.setTitle("Додавання заняття до розкладу");

            NewScheduleController newScheduleController = loader.getController();
            newScheduleController.setSelectedGroup(this.selectedGroup);
            newScheduleController.initialize();

            addScheduleStage.setScene(new Scene(root));

            addScheduleStage.setResizable(false);

            addScheduleStage.showAndWait();
            updateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateData() {
        tableView.setItems(FXCollections.observableArrayList(scheduleService.getGroupSchedule(selectedGroup)));
    }
}

package com.example.studentattendance.gui.controllers.attendance;

import com.example.studentattendance.database.models.dto.AttendanceDTO;
import com.example.studentattendance.database.service.AttendanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller class for managing attendance-related operations in the GUI.
 */
@Component
public class AttendanceController {
    @FXML
    private TableView<AttendanceDTO> tableView;

    @FXML
    private TableColumn<AttendanceDTO, String> statusColumn;

    @Autowired
    private AttendanceService attendanceService;

    private Long selectedScheduleId;

    private final static ObservableList<String> STATUSES =
            FXCollections.observableArrayList("Присутній", "Відсутній", "Запізнення");

    /**
     * Sets the selected schedule ID for the controller.
     *
     * @param scheduleId The ID of the selected schedule.
     */
    public void setSelectedScheduleId(Long scheduleId) {
        this.selectedScheduleId = scheduleId;
    }

    public void updateData() {
        tableView.setItems(FXCollections.observableArrayList(attendanceService.getScheduleAttendance(selectedScheduleId)));
    }

    private void handleStatusChanged() {
        AttendanceDTO attendance = tableView.getSelectionModel().getSelectedItem();
        if (attendance == null) return;

        try {
            attendanceService.updateAttendanceStatus(attendance.getId(), attendance.getStatus());
            updateData();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void initialize() {
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(STATUSES));
        statusColumn.setEditable(true);
        statusColumn.setOnEditCommit(event -> {
            event.getRowValue().setStatus(event.getNewValue());
            handleStatusChanged();
        });
    }
}

package com.example.studentattendance.gui.controllers.student;

import com.example.studentattendance.database.models.dto.StudentDTO;
import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.service.StudentService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller class for displaying students of specific group in the GUI.
 */
@Component
public class StudentsOfGroupController {
    private Group selectedGroup;

    @FXML
    private TableView<StudentDTO> tableView;

    @Autowired
    private StudentService studentService;

    public void initialize() {
        updateData();
    }

    public void updateData() {
        tableView.setItems(FXCollections.observableArrayList(studentService.getGroupStudents(selectedGroup)));
    }

    public void setGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }
}

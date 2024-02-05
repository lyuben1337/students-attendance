package com.example.studentattendance.gui.controllers.group;

import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.service.GroupService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller class for managing group adding in the GUI.
 */
@Component
public class NewGroupController {

    @FXML
    private TextField lastNameField;

    @Autowired
    private GroupService groupService;

    public void handleAddButton() {
        try {
            if (lastNameField.getText().isBlank()) {
                throw new Exception("Назва групи має бути вказана");
            }
            groupService.addGroup(Group.builder()
                    .name(lastNameField.getText())
                    .build());
            Stage stage = (Stage) lastNameField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }
}

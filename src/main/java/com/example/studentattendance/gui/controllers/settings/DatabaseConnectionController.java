package com.example.studentattendance.gui.controllers.settings;

import com.example.studentattendance.database.config.DatabaseConfiguration;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller class for managing database connection settings in the GUI.
 */
@Component
public class DatabaseConnectionController {
    @FXML
    public Button cancelButton;

    @FXML
    private Button connectButton;

    @Autowired
    private DatabaseConfiguration databaseConfiguration;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField urlField;

    public void initialize() {
        urlField.setText("jdbc:postgresql://");

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        });

        connectButton.setOnAction(event -> connectToDataBase());
    }

    @FXML
    private void connectToDataBase() {
        var username = usernameField.getText();
        var password = passwordField.getText();
        var url = urlField.getText();

        try {
            if (username.isBlank() || password.isBlank() || url.isBlank()) {
                throw new Exception("Please provide values for all fields");
            }
            changeDataSource(username, password, url);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Connection failed: " + e.getMessage()).showAndWait();
        }
    }

    private void changeDataSource(String username, String password, String url) {
        try {
            var tenantId = url+username;
            databaseConfiguration.addTenant(tenantId, url, username, password);
            databaseConfiguration.setCurrentTenant(tenantId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

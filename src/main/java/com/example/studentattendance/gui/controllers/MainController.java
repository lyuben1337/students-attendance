package com.example.studentattendance.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MainController {
    @FXML
    private TabPane tabPane;

    @Autowired
    private final ApplicationContext context;

    public void initialize() {
        loadTab("/view/student/students.fxml", "Студенти");
        loadTab("/view/group/groups.fxml", "Групи");
        loadTab("/view/classroom/classrooms.fxml", "Аудиторії");
        loadTab("/view/course/courses.fxml", "Предмети");
    }

    private void loadTab(String fxmlFile, String tabName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            fxmlLoader.setControllerFactory(context::getBean);
            Node content = fxmlLoader.load();
            Tab tab = new Tab(tabName);
            tab.setClosable(false);
            tab.setContent(content);
            tabPane.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTab(Tab newTab) {
        newTab.setClosable(true);
        tabPane.getTabs().add(newTab);
    }

    public void handleSettingsMenu(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the new student form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/settings/database_connection.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            // Create a new stage
            Stage databaseConnection = new Stage();
            databaseConnection.initStyle(StageStyle.UTILITY);
            databaseConnection.initModality(Modality.APPLICATION_MODAL);
            databaseConnection.setTitle("Налаштування бази даних");

            // Set the scene with the loaded FXML content
            databaseConnection.setScene(new Scene(root));
            databaseConnection.setResizable(false);

            // Show the new student form
            databaseConnection.showAndWait();

            tabPane.getTabs().removeAll(tabPane.getTabs());
            initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAboutProgramMenu(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the new student form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/settings/about_program.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            // Create a new stage
            Stage aboutProgram = new Stage();
            aboutProgram.initStyle(StageStyle.UTILITY);
            aboutProgram.initModality(Modality.APPLICATION_MODAL);
            aboutProgram.setTitle("Про програму");

            // Set the scene with the loaded FXML content
            aboutProgram.setScene(new Scene(root));
            aboutProgram.setResizable(false);

            // Show the new student form
            aboutProgram.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

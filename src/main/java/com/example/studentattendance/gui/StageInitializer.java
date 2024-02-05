package com.example.studentattendance.gui;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Component responsible for initializing the main stage of the Student Attendance GUI.
 */
@Component
public class StageInitializer implements ApplicationListener<StudentAttendanceGui.StageReadyEvent> {
    @Autowired
    private ApplicationContext context;

    /**
     * Initializes the main stage of the Student Attendance GUI when the application is ready.
     *
     * @param event The event indicating that the stage is ready.
     */
    @Override
    public void onApplicationEvent(StudentAttendanceGui.StageReadyEvent event) {
        var stage = event.getStage();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_layout.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

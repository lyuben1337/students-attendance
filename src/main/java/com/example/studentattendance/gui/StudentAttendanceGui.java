package com.example.studentattendance.gui;

import com.example.studentattendance.StudentAttendanceApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main class for launching the Student Attendance GUI application.
 */
public class StudentAttendanceGui extends Application {
    private ConfigurableApplicationContext applicationContext;

    /**
     * Initializes the Spring application context.
     */
    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(StudentAttendanceApplication.class).run();
    }

    /**
     * Starts the application, publishing the StageReadyEvent to indicate that the stage is ready.
     *
     * @param stage The main stage of the application.
     */
    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    /**
     * Stops the application, closing the Spring application context and exiting the platform.
     */
    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    /**
     * Custom event class indicating that the application stage is ready.
     */
    static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return (Stage) getSource();
        }
    }
}

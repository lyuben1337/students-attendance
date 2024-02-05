package com.example.studentattendance;


import com.example.studentattendance.gui.StudentAttendanceGui;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentAttendanceApplication {
    public static void main(String[] args) {
        Application.launch(StudentAttendanceGui.class, args);
    }
}

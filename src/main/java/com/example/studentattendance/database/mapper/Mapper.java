package com.example.studentattendance.database.mapper;

import com.example.studentattendance.database.models.dto.AttendanceDTO;
import com.example.studentattendance.database.models.dto.ScheduleDTO;
import com.example.studentattendance.database.models.dto.StudentDTO;
import com.example.studentattendance.database.models.entity.Attendance;
import com.example.studentattendance.database.models.entity.Schedule;
import com.example.studentattendance.database.models.entity.Student;

import java.text.SimpleDateFormat;

/**
 * Mapper class for converting entities to DTOs.
 */
public class Mapper {
    /**
     * Converts a Student entity to a StudentDTO.
     *
     * @param student The Student entity to convert.
     * @return The corresponding StudentDTO.
     */
    public static StudentDTO toStudentDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .name(String.format("%s %s %s", student.getFirstName(), student.getMiddleName(), student.getLastName()))
                .group(student.getGroup().getName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .birthDate(new SimpleDateFormat("dd.MM.yyyy").format(student.getBirthDate()))
                .build();
    }

    /**
     * Converts a Schedule entity to a ScheduleDTO.
     *
     * @param schedule The Schedule entity to convert.
     * @return The corresponding ScheduleDTO.
     */
    public static ScheduleDTO toScheduleDTO(Schedule schedule) {
        return ScheduleDTO.builder()
                .id(schedule.getId())
                .date(new SimpleDateFormat("dd.MM").format(schedule.getDate()))
                .classroom(schedule.getClassroom().getNumber())
                .course(schedule.getCourse().getCode())
                .time(schedule.getStartTime().toString() + " - " + schedule.getEndTime().toString())
                .build();
    }

    /**
     * Converts an Attendance entity to an AttendanceDTO.
     *
     * @param attendance The Attendance entity to convert.
     * @return The corresponding AttendanceDTO.
     */
    public static AttendanceDTO toAttendanceDTO(Attendance attendance) {
        var student = attendance.getStudent();
        var studentName = String.format("%s %s %s", student.getFirstName(), student.getMiddleName(), student.getLastName());

        return AttendanceDTO.builder()
                .id(attendance.getId())
                .studentName(studentName)
                .status(attendance.getStatus())
                .build();
    }
}

package com.example.studentattendance.database.service;

import com.example.studentattendance.database.models.entity.Classroom;
import com.example.studentattendance.database.repository.ClassroomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing classroom-related operations.
 */
@Service
@AllArgsConstructor
public class ClassroomService {
    ClassroomRepository classroomRepository;

    /**
     * Retrieves a list of all classrooms.
     *
     * @return A list of Classroom entities representing all classrooms.
     */
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    /**
     * Adds a new classroom to the system.
     *
     * @param classroom The Classroom entity representing the new classroom.
     * @throws RuntimeException If a classroom with the same number already exists.
     */
    public void addClassroom(Classroom classroom) {
        if (classroomRepository.existsByNumber(classroom.getNumber())) {
            throw new RuntimeException(String.format("Аудиторія з номером %s вже існує", classroom.getNumber()));
        }
        classroomRepository.save(classroom);
    }

    /**
     * Updates the information of an existing classroom.
     *
     * @param classroom The Classroom entity representing the updated information.
     */
    public void updateClassroom(Classroom classroom) {
        classroomRepository.save(classroom);
    }

    /**
     * Deletes a classroom from the system.
     *
     * @param selectedClassroom The Classroom entity representing the classroom to be deleted.
     */
    public void deleteClassroom(Classroom selectedClassroom) {
        classroomRepository.delete(selectedClassroom);
    }
}

package com.example.studentattendance.database.service;

import com.example.studentattendance.database.mapper.Mapper;
import com.example.studentattendance.database.models.dto.StudentDTO;
import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.models.entity.Student;
import com.example.studentattendance.database.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing student-related operations.
 */
@Service
@AllArgsConstructor
public class StudentService {
    StudentRepository studentRepository;

    /**
     * Retrieves a list of all students.
     *
     * @return List of StudentDTO objects representing all students.
     */
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(Mapper::toStudentDTO)
                .toList();
    }

    /**
     * Deletes a student with the specified ID.
     *
     * @param id The ID of the student to be deleted.
     */
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    /**
     * Saves or updates a student.
     *
     * @param newStudent The student to be saved or updated.
     */
    public void saveOrUpdateStudent(Student newStudent) {
        studentRepository.save(newStudent);
    }

    /**
     * Retrieves a list of students belonging to a specific group.
     *
     * @param group The group for which to retrieve students.
     * @return List of StudentDTO objects representing students in the specified group.
     */
    public List<StudentDTO> getGroupStudents(Group group) {
        return studentRepository.findAllByGroup(group)
                .stream()
                .map(Mapper::toStudentDTO)
                .toList();
    }

    /**
     * Retrieves a student with the specified ID.
     *
     * @param selectedStudentId The ID of the student to be retrieved.
     * @return The Student object with the specified ID.
     * @throws RuntimeException if the student with the given ID is not found.
     */
    public Student getStudent(Long selectedStudentId) {
        return studentRepository.findById(selectedStudentId).orElseThrow(
                () -> new RuntimeException("Студента не знайдено")
        );
    }
}

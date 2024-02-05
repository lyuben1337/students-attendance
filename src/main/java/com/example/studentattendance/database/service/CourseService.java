package com.example.studentattendance.database.service;

import com.example.studentattendance.database.models.entity.Course;
import com.example.studentattendance.database.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing course-related operations.
 */
@Service
@AllArgsConstructor
public class CourseService {
    CourseRepository courseRepository;

    /**
     * Retrieves a list of all courses.
     *
     * @return A list of Course entities representing all courses.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Adds a new course to the system.
     *
     * @param course The Course entity representing the new course.
     * @throws RuntimeException If a course with the same code already exists.
     */
    public void addCourse(Course course) {
        if (courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException(String.format("Предмет з кодом %s вже існує", course.getCode()));
        }
        courseRepository.save(course);
    }

    /**
     * Deletes a course from the system.
     *
     * @param selectedCourse The Course entity representing the course to be deleted.
     */
    public void deleteCourse(Course selectedCourse) {
        courseRepository.delete(selectedCourse);
    }
}

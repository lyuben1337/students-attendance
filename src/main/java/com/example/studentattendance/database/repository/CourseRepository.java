package com.example.studentattendance.database.repository;

import com.example.studentattendance.database.models.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);
}
package com.example.studentattendance.database.repository;

import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.models.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByGroup(Group group);
}
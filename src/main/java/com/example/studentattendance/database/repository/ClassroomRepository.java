package com.example.studentattendance.database.repository;

import com.example.studentattendance.database.models.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    boolean existsByNumber(String number);
}
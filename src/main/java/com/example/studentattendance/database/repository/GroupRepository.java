package com.example.studentattendance.database.repository;

import com.example.studentattendance.database.models.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByName(String name);
}
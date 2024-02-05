package com.example.studentattendance.database.repository;

import com.example.studentattendance.database.models.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Transactional
    @Modifying
    @Query("update Attendance a set a.status = ?1 where a.id = ?2")
    void updateStatusById(String status, Long id);
    List<Attendance> findAllBySchedule_IdOrderById(Long scheduleId);
}
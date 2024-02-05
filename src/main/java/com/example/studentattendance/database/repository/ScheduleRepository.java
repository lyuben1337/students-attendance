package com.example.studentattendance.database.repository;

import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.models.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByGroupOrderByDateDescStartTimeAsc(Group group);

    boolean existsByDateAndStartTimeAndGroup(Date date, LocalTime startTime, Group group);
}
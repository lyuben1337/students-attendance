package com.example.studentattendance.database.service;

import com.example.studentattendance.database.mapper.Mapper;
import com.example.studentattendance.database.models.dto.ScheduleDTO;
import com.example.studentattendance.database.models.entity.Attendance;
import com.example.studentattendance.database.models.entity.Group;
import com.example.studentattendance.database.models.entity.Schedule;
import com.example.studentattendance.database.repository.AttendanceRepository;
import com.example.studentattendance.database.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing schedules.
 */
@Service
@AllArgsConstructor
public class ScheduleService {
    private ScheduleRepository scheduleRepository;
    private AttendanceRepository attendanceRepository;

    /**
     * Retrieves the schedule for a specific group.
     *
     * @param group The group for which the schedule is retrieved.
     * @return A list of ScheduleDTO representing the group's schedule.
     * @throws IllegalArgumentException if the provided group is null.
     */
    public List<ScheduleDTO> getGroupSchedule(Group group) {
        return scheduleRepository.findByGroupOrderByDateDescStartTimeAsc(group)
                .stream()
                .map(Mapper::toScheduleDTO)
                .toList();
    }

    /**
     * Deletes a schedule entry by its ID.
     *
     * @param id The ID of the schedule entry to be deleted.
     * @throws IllegalArgumentException if the provided ID is null.
     */
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    /**
     * Adds a new schedule entry and initializes attendance records for associated students.
     *
     * @param schedule The schedule entry to be added.
     * @throws RuntimeException if a schedule entry with the same date, start time, and group already exists.
     * @throws IllegalArgumentException if the provided schedule is null.
     */
    public void addSchedule(Schedule schedule) {
        if (scheduleRepository.existsByDateAndStartTimeAndGroup(
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getGroup())
        ) {
            throw new RuntimeException("На цей час вже існує предмет в роскладі!");
        }
        scheduleRepository.save(schedule);
        schedule.getGroup().getStudents().forEach(student -> attendanceRepository.save(
                Attendance.builder()
                        .schedule(schedule)
                        .student(student)
                        .status("Відсутній")
                        .build()
        ));
    }

    /**
     * Retrieves a schedule entry by its ID.
     *
     * @param id The ID of the schedule entry to be retrieved.
     * @return The Schedule object representing the schedule entry.
     * @throws RuntimeException if the schedule entry with the specified ID is not found.
     * @throws IllegalArgumentException if the provided ID is null.
     */
    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Предмет в роскладі не знайдено"));
    }
}

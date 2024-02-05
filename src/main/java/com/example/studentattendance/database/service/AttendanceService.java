package com.example.studentattendance.database.service;

import com.example.studentattendance.database.mapper.Mapper;
import com.example.studentattendance.database.models.dto.AttendanceDTO;
import com.example.studentattendance.database.repository.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing attendance-related operations.
 */
@Service
@AllArgsConstructor
public class AttendanceService {
    private AttendanceRepository attendanceRepository;

    /**
     * Retrieves the attendance records for a given schedule.
     *
     * @param scheduleId The identifier of the schedule.
     * @return A list of AttendanceDTO representing the attendance records for the specified schedule.
     */
    public List<AttendanceDTO> getScheduleAttendance(Long scheduleId) {
        return attendanceRepository.findAllBySchedule_IdOrderById(scheduleId)
                .stream()
                .map(Mapper::toAttendanceDTO)
                .toList();
    }

    /**
     * Updates the attendance status for a specific attendance record.
     *
     * @param attendanceId The identifier of the attendance record.
     * @param status       The new status to set for the attendance record.
     */
    public void updateAttendanceStatus(Long attendanceId, String status) {
        attendanceRepository.updateStatusById(status, attendanceId);
    }
}

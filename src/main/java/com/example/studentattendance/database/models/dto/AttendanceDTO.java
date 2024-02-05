package com.example.studentattendance.database.models.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class AttendanceDTO {
    private Long id;
    private String studentName;
    private String status;
}

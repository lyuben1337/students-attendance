package com.example.studentattendance.database.models.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class ScheduleDTO {
    private Long id;
    private String course;
    private String classroom;
    private String date;
    private String time;
}

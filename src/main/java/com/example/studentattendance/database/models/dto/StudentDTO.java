package com.example.studentattendance.database.models.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class StudentDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String group;
    private String birthDate;
}

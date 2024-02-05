package com.example.studentattendance.database.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_code")
    private String code;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Schedule> schedules;

    @Override
    public String toString() {
        return code;
    }
}

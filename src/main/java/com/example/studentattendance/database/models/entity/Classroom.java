package com.example.studentattendance.database.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "classrooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_number")
    private String number;

    @Column(name = "capacity")
    private Integer capacity;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.REMOVE)
    private List<Schedule> schedules;

    @Override
    public String toString() {
        return number;
    }
}

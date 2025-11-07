package com.chefbooking.group_5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Chef_Schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChefSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private ChefProfile chef;

    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
}

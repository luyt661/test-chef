package com.chefbooking.group_5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Chef_Profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChefProfile {

    @Id
    @Column(name = "chef_id")
    private Integer chefId; // trùng user_id

    @OneToOne
    @MapsId
    @JoinColumn(name = "chef_id")
    private User user;

    private String bio;
    private Integer experienceYears;
    private String location;
    private String profileImageUrl;
    private Byte status; // 1=PENDING,2=APPROVED,3=REJECTED
    private String specialty;
    private Double averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "chef")
    private List<MenuItem> menuItems;

    @OneToMany(mappedBy = "chef")
    private List<ChefCertificate> certificates;

    @OneToMany(mappedBy = "chef")
    private List<ChefSchedule> schedules;
}

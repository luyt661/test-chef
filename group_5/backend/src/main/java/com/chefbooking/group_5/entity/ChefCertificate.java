package com.chefbooking.group_5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Chef_Certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChefCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer certificateId;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private ChefProfile chef;

    private String title;
    private String description;
    private String certificateUrl;
    private LocalDate issuedDate;
}

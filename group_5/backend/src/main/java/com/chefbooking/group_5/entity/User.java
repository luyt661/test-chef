package com.chefbooking.group_5.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_email", columnNames = {"email"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    // ==========================
    // LOGIN + AUTHENTICATION
    // ==========================
    @Email
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // ==========================
    // USER INFO
    // ==========================
    @NotBlank
    @Size(max = 100)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> userRoles = new ArrayList<>(); // ← THÊM DÒNG NÀY

    @Column(name = "is_active")
    private Boolean isActive = true;

    // ==========================
    // ADDITIONAL FIELDS FROM ERD
    // ==========================
    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    private LocalDate dateOfBirth;

    @Column(length = 500)
    private String address;

    // ==========================
    // AUDIT
    // ==========================
    @Column(name = "created_at", columnDefinition = "DATETIME2")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "DATETIME2")
    private LocalDateTime updatedAt;

    // ==========================
    // RELATIONSHIPS
    // ==========================

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ChefProfile chefProfile;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    public List<String> getRoleNames() {
        if (userRoles == null) return List.of();
        return userRoles.stream()
                .map(ur -> ur.getRole().getRoleName())
                .toList();
    }

}

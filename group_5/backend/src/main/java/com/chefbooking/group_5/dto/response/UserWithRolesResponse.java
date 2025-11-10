package com.chefbooking.group_5.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRolesResponse {
    private Integer userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Boolean isActive;
    private String profileImageUrl;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Để dễ cập nhật phân quyền
    private Integer roleIds;

    // Để hiển thị trên UI
    private String roleNames;

}

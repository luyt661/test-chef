package com.chefbooking.group_5.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Integer userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String profileImageUrl;
    private Byte role;
    private Boolean isActive;
}

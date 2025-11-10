package com.chefbooking.group_5.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AdminUpdateProfileRequest {
    @Size(max = 100)
    @NotBlank(message = "Email không được để trống")
    private String email;
    private boolean active;
    @NotBlank(message = "Full Name không được để trống")
    private String fullname;
    private String image_url;
    @Size(max = 20)
    @NotBlank(message = "Phone không được để trống")
    private String phone;
    private LocalDate dateOfBirth;
    @NotBlank(message = "Address không được để trống")
    private String address;
    private Integer roleIds;
}
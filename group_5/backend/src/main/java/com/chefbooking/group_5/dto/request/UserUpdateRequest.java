package com.chefbooking.group_5.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserUpdateRequest {
    @Size(max = 100)
    private String fullname;

    @Size(max = 20)
    private String phone;

    private LocalDate dateOfBirth;

    private String address;
}
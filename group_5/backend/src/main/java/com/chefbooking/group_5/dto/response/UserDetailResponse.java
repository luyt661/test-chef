package com.chefbooking.group_5.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class UserDetailResponse {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String profileUrl;
    private LocalDate dateOfBirth;
    private String address;
}
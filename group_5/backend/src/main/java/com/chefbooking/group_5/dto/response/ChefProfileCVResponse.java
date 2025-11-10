package com.chefbooking.group_5.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChefProfileCVResponse {
    private Integer chefId;
    private String fullName;
    private String email;
    private String bio;
    private Integer experienceYears;
    private String location;
    private String specialty;
    private Byte status;
    private String profileImageUrl;
    private List<ChefCertificateResponse> certificates;
}

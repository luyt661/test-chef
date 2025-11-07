package com.chefbooking.group_5.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private Integer userId;
    private String email;
    private List<String> roles;
}


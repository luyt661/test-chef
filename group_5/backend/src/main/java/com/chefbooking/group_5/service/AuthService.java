package com.chefbooking.group_5.service;

import com.chefbooking.group_5.dto.request.LoginRequest;
import com.chefbooking.group_5.dto.request.RegisterRequest;
import com.chefbooking.group_5.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse authenticate(LoginRequest request);

    TokenResponse register(RegisterRequest request);
}

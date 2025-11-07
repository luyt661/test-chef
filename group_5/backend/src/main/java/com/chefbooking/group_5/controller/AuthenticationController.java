package com.chefbooking.group_5.controller;

import com.chefbooking.group_5.dto.request.LoginRequest;
import com.chefbooking.group_5.dto.request.RegisterRequest;
import com.chefbooking.group_5.dto.request.TokenRefreshRequest;
import com.chefbooking.group_5.dto.response.ResponseData;
import com.chefbooking.group_5.dto.response.TokenResponse;
import com.chefbooking.group_5.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseData<TokenResponse> login(@Valid @RequestBody LoginRequest request){

        try {
            TokenResponse tokenResponse = authService.authenticate(request);
            return new ResponseData<>(HttpStatus.OK.value(),"Login successfully", tokenResponse);
        } catch (Exception e) {
            return new ResponseData(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseData<TokenResponse> register(@Valid @RequestBody RegisterRequest request){
        try {
            TokenResponse tokenResponse = authService.register(request);
            return new ResponseData<>(HttpStatus.CREATED.value(),"Register successfully", tokenResponse);
        } catch (Exception e) {
            return new ResponseData(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}

package com.chefbooking.group_5.service;

import com.chefbooking.group_5.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String generate(User user);
    String extractUsername(String token);
    Boolean validate(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
}


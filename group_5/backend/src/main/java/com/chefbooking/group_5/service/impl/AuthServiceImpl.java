package com.chefbooking.group_5.service.impl;

import com.chefbooking.group_5.dto.request.LoginRequest;
import com.chefbooking.group_5.dto.request.RegisterRequest;
import com.chefbooking.group_5.dto.response.TokenResponse;
import com.chefbooking.group_5.entity.Role;
import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.entity.UserRole;
import com.chefbooking.group_5.repository.RoleRepository;
import com.chefbooking.group_5.repository.UserRepository;
import com.chefbooking.group_5.repository.UserRoleRepository;
import com.chefbooking.group_5.service.AuthService;
import com.chefbooking.group_5.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("User or Password incorrect");
        }

        String accessToken = jwtService.generate(user);

        var roles = Optional.ofNullable(user.getUserRoles())
                .orElse(Collections.emptyList())
                .stream()
                .map(ur -> "ROLE_" + ur.getRole().getRoleName())
                .toList();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken("temp-refresh-token")
                .userId(user.getUserId())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setIsActive(true);

        userRepository.save(user);

        // TỰ ĐỘNG GÁN ROLE_USER
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleName("USER");
                    return roleRepository.save(role);
                });

        UserRole ur = new UserRole();
        ur.setUser(user);
        ur.setRole(userRole);
        userRoleRepository.save(ur);

        // GÁN ROLE_ADMIN CHO USER ĐẦU TIÊN
        if (userRepository.count() == 1) {
            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRoleName("ADMIN");
                        return roleRepository.save(role);
                    });

            UserRole adminUr = new UserRole();
            adminUr.setUser(user);
            adminUr.setRole(adminRole);
            userRoleRepository.save(adminUr);
        }

        String accessToken = jwtService.generate(user);

        var roles = Optional.ofNullable(user.getUserRoles())
                .orElse(Collections.emptyList())
                .stream()
                .map(r -> "ROLE_" + r.getRole().getRoleName())
                .toList();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken("temp-refresh-token")
                .userId(user.getUserId())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
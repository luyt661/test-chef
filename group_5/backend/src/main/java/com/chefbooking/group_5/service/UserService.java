package com.chefbooking.group_5.service;

import com.chefbooking.group_5.dto.*;
import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.repository.UserRepository;
import com.chefbooking.group_5.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ========== ĐĂNG KÝ ==========
    public UserProfileDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setRole((byte) 3); // 3 = CUSTOMER
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return mapToUserProfileDTO(user);
    }

    // ========== ĐĂNG NHẬP ==========
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            throw new RuntimeException("Email không tồn tại!");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("Tài khoản đã bị khóa!");
        }

        // Sinh JWT thật
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        AuthResponse res = new AuthResponse();
        res.setAccessToken(token);
        res.setUserId(user.getUserId());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        return res;
    }

    // ========== XEM THÔNG TIN ==========
    public UserProfileDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        return mapToUserProfileDTO(user);
    }

    // Lấy theo email
    public UserProfileDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("Không tìm thấy user");
        return mapToUserProfileDTO(user);
    }

    // ========== CẬP NHẬT HỒ SƠ ==========
    public UserProfileDTO updateProfile(Integer id, UpdateProfileRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getProfileImageUrl() != null) user.setProfileImageUrl(request.getProfileImageUrl());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return mapToUserProfileDTO(user);
    }

    // Cập nhật theo email (dùng cho endpoint /me)
    public UserProfileDTO updateProfileByEmail(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("Không tìm thấy user");

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getProfileImageUrl() != null) user.setProfileImageUrl(request.getProfileImageUrl());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return mapToUserProfileDTO(user);
    }

    // ========== HÀM HỖ TRỢ ==========
    private UserProfileDTO mapToUserProfileDTO(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setRole(user.getRole());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}

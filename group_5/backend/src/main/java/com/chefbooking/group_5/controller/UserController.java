package com.chefbooking.group_5.controller;

import com.chefbooking.group_5.dto.request.ChangePasswordRequest;
import com.chefbooking.group_5.dto.request.UserUpdateRequest;
import com.chefbooking.group_5.dto.response.ResponseData;
import com.chefbooking.group_5.dto.response.UserDetailResponse;
import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.service.JwtService;
import com.chefbooking.group_5.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Thiếu hoặc định dạng token không hợp lệ");
        }
        String token = authHeader.substring(7);

        // SỬA: chỉ dùng token
        if (jwtService.isTokenExpired(token)) {
            throw new IllegalArgumentException("Token đã hết hạn");
        }

        String username = jwtService.extractUsername(token); // SỬA: không cần TokenType
        if (username == null) {
            throw new IllegalArgumentException("Token không hợp lệ");
        }
        return username;
    }

    @GetMapping("/profile")
    public ResponseData<UserDetailResponse> getProfile(Authentication authentication) {
        String email = authentication.getName(); // ← LẤY TỪ SECURITY CONTEXT
        User user = userService.findByUsername(email);

        UserDetailResponse dto = UserDetailResponse.builder()
                .id(user.getUserId().longValue())
                .fullname(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .profileUrl(user.getProfileImageUrl())
                .dateOfBirth(user.getDateOfBirth())
                .build();

        return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin hồ sơ thành công", dto);
    }

    @PutMapping("/profile")
    public ResponseData<UserDetailResponse> updateProfile(
            @RequestBody @Valid UserUpdateRequest updateRequest,
            HttpServletRequest request) {

        String username = extractToken(request);
        UserDetailResponse updatedDto = userService.updateProfileByUsername(username, updateRequest);

        return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật hồ sơ thành công", updatedDto);
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(request);
            // Có thể trả về DTO Response nếu muốn
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            // Nên có một GlobalExceptionHandler để xử lý
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
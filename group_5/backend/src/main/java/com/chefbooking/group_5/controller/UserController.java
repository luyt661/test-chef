//package com.chefbooking.group_5.controller;
//
//import com.chefbooking.group_5.dto.*;
//import com.chefbooking.group_5.dto.request.LoginRequest;
//import com.chefbooking.group_5.dto.request.RegisterRequest;
//import com.chefbooking.group_5.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
//        return ResponseEntity.ok(userService.register(request));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
//        return ResponseEntity.ok(userService.login(request));
//    }
//
//    // Lấy thông tin user hiện tại từ token
//    @GetMapping("/getprofile")
//    public ResponseEntity<?> getMyProfile(@RequestHeader(value = "Authorization") String authHeader) {
//        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
//        String email = jwtUtil.extractEmail(token);
//        return ResponseEntity.ok(userService.getUserByEmail(email));
//    }
//
//    // Cập nhật hồ sơ user hiện tại
//    @PutMapping("/updateprofile")
//    public ResponseEntity<?> updateMyProfile(@RequestHeader(value = "Authorization") String authHeader,
//                                             @RequestBody UpdateProfileRequest request) {
//        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
//        String email = jwtUtil.extractEmail(token);
//        return ResponseEntity.ok(userService.updateProfileByEmail(email, request));
//    }
//}

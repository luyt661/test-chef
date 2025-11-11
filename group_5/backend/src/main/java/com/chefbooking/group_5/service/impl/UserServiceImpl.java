package com.chefbooking.group_5.service.impl;

import com.chefbooking.group_5.dto.request.ChangePasswordRequest;
import com.chefbooking.group_5.dto.request.RegisterRequest;
import com.chefbooking.group_5.dto.request.UserUpdateRequest;
import com.chefbooking.group_5.dto.response.UserDetailResponse;
import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.repository.UserRepository;
import com.chefbooking.group_5.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import com.chefbooking.group_5.service.FileUploadService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final FileUploadService fileUploadService;

    @Override
    public UserDetailsService getUserDetailsService() {
        return email -> {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("Email not found: " + email);
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getUserRoles().stream()
                            .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getRoleName()))
                            .toList()
            );
        };
    }
    @Override
    public User findByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng: " + email);
        }
        return user;
    }

    @Override
    @Transactional
    public UserDetailResponse updateProfileByUsername(String email, UserUpdateRequest request) {
        // 1. Tìm user theo email
        User user = findByUsername(email);

        // 2. Cập nhật thông tin nếu có
        if (request.getFullname() != null) user.setFullName(request.getFullname());
        if (request.getPhone() != null) user.setPhoneNumber(request.getPhone());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) user.setAddress(request.getAddress());

        // 3. Lưu user
        userRepository.save(user);

        // 4. Build UserDetailResponse để trả về
        return UserDetailResponse.builder()
                .id(user.getUserId().longValue())
                .fullname(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .profileUrl(user.getProfileImageUrl())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        // 1. Lấy thông tin user đang đăng nhập
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername(); // Đây chính là email

        // 2. Tìm user bằng phương thức BẠN ĐÃ VIẾT SẴN (findByUsername)
        // Phương thức này đã tự xử lý "orElseThrow" rồi
        User user = findByUsername(email);

        // 3. Kiểm tra mật khẩu cũ
        // Dùng getPasswordHash() để khớp với entity User của bạn
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Incorrect old password");
        }

        // 4. Kiểm tra mật khẩu mới và xác nhận
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // 5. Cập nhật và lưu mật khẩu mới
        // Dùng setPasswordHash() để khớp với entity User của bạn
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional // quan trọng vì cập nhật CSDL
    public UserDetailResponse updateProfileImage(String email, MultipartFile file) throws IOException {

        // 1. Tải ảnh lên Cloudinary
        String imageUrl = fileUploadService.uploadFile(file);

        // 2. Tìm user
        User user = findByUsername(email); // Dùng lại phương thức của bạn

        // 3. Cập nhật URL ảnh mới
        user.setProfileImageUrl(imageUrl);

        // 4. Lưu lại vào database
        userRepository.save(user);

        // 5. Trả về DTO với thông tin mới
        return UserDetailResponse.builder()
                .id(user.getUserId().longValue())
                .fullname(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .profileUrl(user.getProfileImageUrl()) // Đây là URL mới
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }
}

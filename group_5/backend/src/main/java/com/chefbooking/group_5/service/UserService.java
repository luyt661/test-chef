package com.chefbooking.group_5.service;

import com.chefbooking.group_5.dto.request.ChangePasswordRequest;
import com.chefbooking.group_5.dto.request.RegisterRequest;
import com.chefbooking.group_5.dto.request.UserUpdateRequest;
import com.chefbooking.group_5.dto.response.UserDetailResponse;
import com.chefbooking.group_5.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService getUserDetailsService();
    User findByUsername(String email);                    // THÊM
    UserDetailResponse updateProfileByUsername(String email, UserUpdateRequest request);

    void changePassword(ChangePasswordRequest request);
}

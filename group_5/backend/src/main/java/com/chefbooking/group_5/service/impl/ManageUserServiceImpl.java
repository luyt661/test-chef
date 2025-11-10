package com.chefbooking.group_5.service.impl;

import com.chefbooking.group_5.dto.request.AdminUpdateProfileRequest;
import com.chefbooking.group_5.dto.response.UserPageResponse;
import com.chefbooking.group_5.dto.response.UserWithRolesResponse;
import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.entity.UserRole;
import com.chefbooking.group_5.repository.UserRepository;
import com.chefbooking.group_5.service.ManageUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageUserServiceImpl implements ManageUserService {

    private final  UserRepository userRepository;
    @Override
    public UserPageResponse getActiveUsersWithRoles(int page, int size) {
        return getPagedUsers(true, null, page, size);
    }

    @Override
    public UserPageResponse getActiveChef(int page, int size) {
        return getPagedUsers(true, 2, page, size);
    }

    @Override
    public UserPageResponse getActiveUser(int page, int size) {
        return getPagedUsers(true, 4, page, size);
    }

    @Override
    public UserPageResponse getActiveAdmin(int page, int size) {
        return getPagedUsers(true, 1, page, size);
    }

    @Override
    public UserPageResponse getInactiveUsersWithRoles(int page, int size) {
        return getPagedUsers(false, null, page, size);
    }

    private UserPageResponse getPagedUsers(boolean active, Integer roleId, int page, int size) {
        // Lấy tất cả user active kèm roles
        List<User> allUsers = (active == true) ? userRepository.findActiveUsersWithRoles() : userRepository.findInactiveUsersWithRoles();

        List<UserWithRolesResponse> filtered = new ArrayList<>();

        // Lọc user theo role (nếu có)
        for (User user : allUsers) {
            boolean match = (roleId == null); // true nếu không lọc role
            Integer roleIdOfUser = null;
            String roleNameOfUser = null;

            if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
                // Mỗi user chỉ có 1 role -> lấy role đầu tiên
                UserRole ur = user.getUserRoles().get(0);

                if (ur != null && ur.getRole() != null) {
                    roleIdOfUser = ur.getRole().getId();
                    roleNameOfUser = ur.getRole().getRoleName();

                    // Nếu có lọc roleId cụ thể
                    if (roleId != null && roleIdOfUser.equals(roleId)) {
                        match = true;
                    }
                }
            }

            // Nếu user phù hợp filter thì thêm vào danh sách kết quả
            if (match) {
                UserWithRolesResponse u = getUserWithRolesResponse(user, roleIdOfUser, roleNameOfUser);
                filtered.add(u);
            }

        }

        // Phân trang thủ công (in-memory)
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int from = page * size;
        int to = Math.min(from + size, total);

        List<UserWithRolesResponse> pagedList = (from < total)
                ? filtered.subList(from, to)
                : new ArrayList<>();

        // Gói kết quả trả về
        UserPageResponse response = new UserPageResponse();
        response.setUsers(pagedList);
        response.setCurrentPage(page);
        response.setPageSize(size);
        response.setTotalElements(total);
        response.setTotalPages(totalPages);
        return response;
    }

    private UserWithRolesResponse getUserWithRolesResponse(User user,Integer roleIds,String roleNames) {
        UserWithRolesResponse u = new UserWithRolesResponse();
        u.setUserId(user.getUserId());
        u.setFullName(user.getFullName());
        u.setEmail(user.getEmail());
        u.setPhoneNumber(user.getPhoneNumber());
        u.setIsActive(user.getIsActive());
        u.setProfileImageUrl(user.getProfileImageUrl());
        u.setDateOfBirth(user.getDateOfBirth());
        u.setAddress(user.getAddress());
        u.setCreatedAt(user.getCreatedAt());
        u.setUpdatedAt(user.getUpdatedAt());
        u.setRoleIds(roleIds);
        u.setRoleNames(roleNames);
        return u;
    }


    @Override
    public UserWithRolesResponse getUserDetail(Integer userID) {
        User user = userRepository.findById(userID).orElse(null);

        Integer roleId = null;
        String roleName = null;
        if (user != null) {
            if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
                UserRole ur = user.getUserRoles().get(0);
                if (ur != null && ur.getRole() != null) {
                    roleId = ur.getRole().getId();
                    roleName = ur.getRole().getRoleName();
                }
            }
            return getUserWithRolesResponse(user,roleId,roleName);
        }
        return null;
    }

    @Transactional
    @Override
    public UserWithRolesResponse updateProfileByAdmin(Integer userID, AdminUpdateProfileRequest request) {
        User user = userRepository.findById(userID).orElse(null);
        Integer roleId = null;
        String roleName = null;
        if (user != null) {
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                user.setEmail(request.getEmail().trim());
            }
            if (request.getFullname() != null && !request.getFullname().isBlank()) {
                user.setFullName(request.getFullname().trim());
            }
            if (request.getPhone() != null && !request.getPhone().isBlank()) {
                user.setPhoneNumber(request.getPhone().trim());
            }
            if (request.getAddress() != null && !request.getAddress().isBlank()) {
                user.setAddress(request.getAddress().trim());
            }
            if (request.getImage_url() != null && !request.getImage_url().isBlank()) {
                user.setProfileImageUrl(request.getImage_url().trim());
            }
            if (request.getDateOfBirth() != null) {
                user.setDateOfBirth(request.getDateOfBirth());
            }
            user.setIsActive(request.isActive());
            user.setUpdatedAt(LocalDateTime.now());
            if (request.getRoleIds() != null) {
                userRepository.updateRoleByUserId(user.getUserId(), request.getRoleIds());
            }

            userRepository.save(user);
        } else {
            return null;
        }
        User newUser = userRepository.findById(userID).orElse(null);
        if (newUser.getUserRoles() != null && !newUser.getUserRoles().isEmpty()) {
            UserRole ur = newUser.getUserRoles().get(0);
            if (ur != null && ur.getRole() != null) {
                roleId = ur.getRole().getId();
                roleName = ur.getRole().getRoleName();
            }
        }
        return getUserWithRolesResponse(newUser,roleId,roleName);
    }
}

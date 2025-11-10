package com.chefbooking.group_5.controller;

import com.chefbooking.group_5.dto.request.AdminUpdateProfileRequest;
import com.chefbooking.group_5.dto.response.UserPageResponse;
import com.chefbooking.group_5.dto.response.UserWithRolesResponse;
import com.chefbooking.group_5.service.ManageUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class ManageUserController {
    private final ManageUserService manageUserService;

    @GetMapping("/active")
    public ResponseEntity<UserPageResponse> getActiveUsersWithRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageUserService.getActiveUsersWithRoles(page, size));
    }

    @GetMapping("/inactive")
    public ResponseEntity<UserPageResponse> getInactiveUsersWithRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageUserService.getInactiveUsersWithRoles(page, size));
    }

    @GetMapping("/activeChef")
    public ResponseEntity<UserPageResponse> getActiveChef(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageUserService.getActiveChef(page, size));
    }

    @GetMapping("/activeUser")
    public ResponseEntity<UserPageResponse> getActiveUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageUserService.getActiveUser(page, size));
    }

    @GetMapping("/activeAdmin")
    public ResponseEntity<UserPageResponse> getActiveAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageUserService.getActiveAdmin(page, size));
    }

    @GetMapping("/userId={userId}")
    public ResponseEntity<UserWithRolesResponse> getUserDetail(@PathVariable Integer userId) {
        UserWithRolesResponse response = manageUserService.getUserDetail(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/userId={userId}")
    public ResponseEntity<UserWithRolesResponse> updateUserByAdmin(@PathVariable Integer userId,@Valid @RequestBody AdminUpdateProfileRequest request) {
        UserWithRolesResponse response = manageUserService.updateProfileByAdmin(userId, request);
        return ResponseEntity.ok(response);
    }

}

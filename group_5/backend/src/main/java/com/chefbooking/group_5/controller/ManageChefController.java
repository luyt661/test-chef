package com.chefbooking.group_5.controller;

import com.chefbooking.group_5.dto.request.AdminUpdateProfileRequest;
import com.chefbooking.group_5.dto.request.AdminUpdateStatusCVRequest;
import com.chefbooking.group_5.dto.response.ChefPageResponse;
import com.chefbooking.group_5.dto.response.ChefProfileCVResponse;
import com.chefbooking.group_5.dto.response.UserPageResponse;
import com.chefbooking.group_5.dto.response.UserWithRolesResponse;
import com.chefbooking.group_5.service.ManageChefService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/chefs")
@RequiredArgsConstructor
public class ManageChefController {
    private final ManageChefService manageChefService;

    @GetMapping("/cvpending")
    public ResponseEntity<ChefPageResponse> getChefProfilesPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageChefService.getChefProfileCVResponse((byte) 1,page, size));
    }

    @GetMapping("/cvapprove")
    public ResponseEntity<ChefPageResponse> getChefProfilesApprove(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageChefService.getChefProfileCVResponse((byte) 2,page, size));
    }

    @GetMapping("/cvreject")
    public ResponseEntity<ChefPageResponse> getChefProfilesReject(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageChefService.getChefProfileCVResponse((byte) 3,page, size));
    }

    @GetMapping("/chefId={chefId}")
    public ResponseEntity<ChefProfileCVResponse> getUserDetail(@PathVariable Integer chefId) {
        ChefProfileCVResponse response = manageChefService.getChefCVDetail(chefId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/chefId={chefId}")
    public ResponseEntity<ChefProfileCVResponse> updateUserByAdmin(@PathVariable Integer chefId,@Valid @RequestBody AdminUpdateStatusCVRequest request) {
        ChefProfileCVResponse response = manageChefService.updateStatusCVAndRoleByAdmin(chefId, request);
        return ResponseEntity.ok(response);
    }
}

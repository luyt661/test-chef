package com.chefbooking.group_5.service;

import com.chefbooking.group_5.dto.request.AdminUpdateProfileRequest;
import com.chefbooking.group_5.dto.request.AdminUpdateStatusCVRequest;
import com.chefbooking.group_5.dto.response.ChefPageResponse;
import com.chefbooking.group_5.dto.response.ChefProfileCVResponse;
import com.chefbooking.group_5.dto.response.UserWithRolesResponse;
import com.chefbooking.group_5.entity.ChefProfile;

import java.util.List;

public interface ManageChefService {
    public ChefPageResponse getChefProfileCVResponse(Byte status, int page, int size);
    public ChefProfileCVResponse updateStatusCVAndRoleByAdmin(Integer chefId, AdminUpdateStatusCVRequest request);
    public ChefProfileCVResponse getChefCVDetail(Integer chefID);
}

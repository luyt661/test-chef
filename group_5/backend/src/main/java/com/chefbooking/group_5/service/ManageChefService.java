package com.chefbooking.group_5.service;

import com.chefbooking.group_5.dto.request.AdminUpdateStatusCVRequest;
import com.chefbooking.group_5.dto.response.ChefPageResponse;
import com.chefbooking.group_5.dto.response.ChefProfileCVResponse;

public interface ManageChefService {
    ChefPageResponse getChefProfileCVResponse(Byte status, int page, int size);
    ChefProfileCVResponse updateStatusCVAndRoleByAdmin(Integer chefId, AdminUpdateStatusCVRequest request);
    ChefProfileCVResponse getChefCVDetail(Integer chefID);
}
package com.chefbooking.group_5.service;

import com.chefbooking.group_5.dto.request.AdminUpdateProfileRequest;
import com.chefbooking.group_5.dto.response.UserPageResponse;
import com.chefbooking.group_5.dto.response.UserWithRolesResponse;

import java.util.List;

public interface ManageUserService {
    public UserPageResponse getActiveUsersWithRoles(int page, int size);
    public UserPageResponse  getActiveChef(int page, int size);
    public UserPageResponse  getActiveUser(int page, int size);
    public UserPageResponse  getActiveAdmin(int page, int size);
    public UserPageResponse getInactiveUsersWithRoles(int page, int size);
    UserWithRolesResponse updateProfileByAdmin(Integer userId, AdminUpdateProfileRequest request);
    public UserWithRolesResponse getUserDetail(Integer userID);

}

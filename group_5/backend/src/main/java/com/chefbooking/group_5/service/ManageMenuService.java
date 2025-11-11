package com.chefbooking.group_5.service;

import com.chefbooking.group_5.dto.request.AdminUpdateStatusMenuItemRequest;
import com.chefbooking.group_5.dto.response.ManageMenuItemResponse;
import com.chefbooking.group_5.dto.response.MenuItemsPageResponse;
import jakarta.validation.Valid;

public interface ManageMenuService {
    MenuItemsPageResponse getMenuItemOfChefByAdmin(Integer chefId, Byte status, int page, int size);
    ManageMenuItemResponse getMenuItemsDetailsOfChefByAdmin(Integer menuItemId);
    ManageMenuItemResponse updateMenuItemsStatusByAdmin(Integer menuItemId, @Valid AdminUpdateStatusMenuItemRequest request);
}

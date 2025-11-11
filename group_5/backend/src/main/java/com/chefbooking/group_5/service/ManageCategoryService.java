package com.chefbooking.group_5.service;

import com.chefbooking.group_5.dto.request.AdminAddNewCategoryRequest;
import com.chefbooking.group_5.dto.response.ManageCategoryResponse;
import com.chefbooking.group_5.dto.response.MenuItemsPageResponse;

import java.util.List;

public interface ManageCategoryService {
    List<ManageCategoryResponse> getAllCategoryByAdmin();
    List<ManageCategoryResponse> addNewCategoryByAdmin(AdminAddNewCategoryRequest request);
    List<ManageCategoryResponse> searchCategoryByAdmin(String categoryName);
    ManageCategoryResponse updateCategoryByAdmin(Integer categoryId, AdminAddNewCategoryRequest request);
    ManageCategoryResponse getCategoryDetailByAdmin(Integer categoryId);
}

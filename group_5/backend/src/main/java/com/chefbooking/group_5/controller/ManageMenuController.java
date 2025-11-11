package com.chefbooking.group_5.controller;

import com.chefbooking.group_5.dto.request.AdminAddNewCategoryRequest;
import com.chefbooking.group_5.dto.request.AdminUpdateProfileRequest;
import com.chefbooking.group_5.dto.request.AdminUpdateStatusMenuItemRequest;
import com.chefbooking.group_5.dto.response.ManageCategoryResponse;
import com.chefbooking.group_5.dto.response.ManageMenuItemResponse;
import com.chefbooking.group_5.dto.response.MenuItemsPageResponse;
import com.chefbooking.group_5.dto.response.UserWithRolesResponse;
import com.chefbooking.group_5.service.ManageCategoryService;
import com.chefbooking.group_5.service.ManageMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menu")
@RequiredArgsConstructor
public class ManageMenuController {

    private final ManageMenuService manageMenuService;
    private final ManageCategoryService manageCategoryService;

    @GetMapping("/allcategory")
    public ResponseEntity<List<ManageCategoryResponse>> getChefProfilesReject() {
        return ResponseEntity.ok(manageCategoryService.getAllCategoryByAdmin());
    }

    @PutMapping("/addsingle-category")
    public ResponseEntity<List<ManageCategoryResponse>> addSingleCategory(@Valid @RequestBody AdminAddNewCategoryRequest request) {
        manageCategoryService.addNewCategoryByAdmin(request);
        return ResponseEntity.ok(manageCategoryService.getAllCategoryByAdmin());
    }

    @GetMapping("/categoryName={categoryName}")
    public ResponseEntity<List<ManageCategoryResponse>> addSingleCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(manageCategoryService.searchCategoryByAdmin(categoryName));
    }

    @GetMapping("/categoryId={categoryId}")
    public ResponseEntity<ManageCategoryResponse> getUserDetail(@PathVariable Integer categoryId) {
        ManageCategoryResponse response = manageCategoryService.getCategoryDetailByAdmin(categoryId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categoryId={categoryId}")
    public ResponseEntity<ManageCategoryResponse> updateUserByAdmin(@PathVariable Integer categoryId,@Valid @RequestBody AdminAddNewCategoryRequest request) {
        ManageCategoryResponse response = manageCategoryService.updateCategoryByAdmin(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/menuList")
    public ResponseEntity<MenuItemsPageResponse> getMenuItemsOfChefByAdmin(
            @RequestParam Integer chefId,
            @RequestParam Byte status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(manageMenuService.getMenuItemOfChefByAdmin(chefId, status, page, size));
    }

    @GetMapping("/menuItemId={menuItemId}")
    public ResponseEntity<ManageMenuItemResponse> getMenuItemsDetailOfChefByAdmin( @PathVariable Integer menuItemId) {
        return ResponseEntity.ok(manageMenuService.getMenuItemsDetailsOfChefByAdmin(menuItemId));
    }

    @PutMapping("/menuItemId={menuItemId}")
    public ResponseEntity<ManageMenuItemResponse> updateMenuItemsDetailOfChefByAdmin( @PathVariable Integer menuItemId, @RequestBody AdminUpdateStatusMenuItemRequest request) {
        return ResponseEntity.ok(manageMenuService.updateMenuItemsStatusByAdmin(menuItemId, request));
    }

}

package com.chefbooking.group_5.service.impl;

import com.chefbooking.group_5.dto.request.AdminUpdateStatusCVRequest;
import com.chefbooking.group_5.dto.request.AdminUpdateStatusMenuItemRequest;
import com.chefbooking.group_5.dto.response.*;
import com.chefbooking.group_5.entity.ChefProfile;
import com.chefbooking.group_5.entity.MenuItem;
import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.repository.ManageMenuRepository;
import com.chefbooking.group_5.service.ManageMenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageMenuServiceImpl implements ManageMenuService {

    private final ManageMenuRepository manageMenuRepository;

    @Override
    public MenuItemsPageResponse getMenuItemOfChefByAdmin(Integer chefId, Byte status, int page, int size) {
        List<MenuItem> menuItemList = manageMenuRepository.findMenuItemsByChefAndStatus(chefId, status);
        System.out.println("Found menu items: " + menuItemList.size()
                + " for chefId=" + chefId + ", status=" + status);
        List<ManageMenuItemResponse> menuItemResponseList = new ArrayList<>();

        for (MenuItem item : menuItemList) {
            List<ManageMenuItemImagesResponse> menuItemImageList = manageMenuRepository.getMenuItemImageResponse(item.getMenuItemId());
            menuItemResponseList.add(getManageMenuItemResponse(item, menuItemImageList));
        }

        // Phân trang thủ công (in-memory)
        int total = menuItemResponseList.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int from = page * size;
        int to = Math.min(from + size, total);

        List<ManageMenuItemResponse> pagedList = (from < total)
                ? menuItemResponseList.subList(from, to)
                : new ArrayList<>();
        // Gói kết quả trả về
        MenuItemsPageResponse response = new MenuItemsPageResponse();
        response.setMenuItemList(pagedList);
        response.setCurrentPage(page);
        response.setPageSize(size);
        response.setTotalElements(total);
        response.setTotalPages(totalPages);

        return response;
    }

    @Override
    public ManageMenuItemResponse getMenuItemsDetailsOfChefByAdmin(Integer menuItemId) {
        MenuItem item = manageMenuRepository.findByMenuItemId(menuItemId);
        if (item != null) {
            List<ManageMenuItemImagesResponse> menuItemImageList = manageMenuRepository.getMenuItemImageResponse(menuItemId);
            return getManageMenuItemResponse(item, menuItemImageList);
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public ManageMenuItemResponse updateMenuItemsStatusByAdmin(Integer menuItemId, AdminUpdateStatusMenuItemRequest request) {
            manageMenuRepository.updateMenuItemStatusAndReasonNative(menuItemId, request.getStatus(),request.getRejectionReason());
            return getMenuItemsDetailsOfChefByAdmin(menuItemId);
    }

    private ManageMenuItemResponse getManageMenuItemResponse(MenuItem menuItem, List<ManageMenuItemImagesResponse> manageMenuItemImagesResponse) {
        ManageMenuItemResponse m = new ManageMenuItemResponse();
        m.setMenuItemId(menuItem.getMenuItemId());
        if (menuItem.getChef() != null) {
            m.setChefId(menuItem.getChef().getChefId());
        }
        if (menuItem.getCategory() != null) {
            m.setCategoryId(menuItem.getCategory().getCategoryId());
        }
        m.setTitle(menuItem.getTitle());
        m.setDescription(menuItem.getDescription());
        m.setPrice(menuItem.getPrice());
        m.setDurationHours(menuItem.getDurationHours());
        m.setStatus(menuItem.getStatus());
        m.setRejectionReason(menuItem.getRejectionReason());
        m.setCreatedDate(menuItem.getCreatedAt());
        m.setUpdatedDate(menuItem.getUpdatedAt());
        m.setMenuItemImagesResponseList(manageMenuItemImagesResponse);
        return m;
    }
}

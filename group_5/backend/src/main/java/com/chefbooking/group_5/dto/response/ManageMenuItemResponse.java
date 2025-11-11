package com.chefbooking.group_5.dto.response;

import com.chefbooking.group_5.entity.Category;
import com.chefbooking.group_5.entity.ChefProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageMenuItemResponse {
    private Integer menuItemId;
    private Integer chefId;
    private Integer categoryId;
    private String title;
    private String description;
    private BigDecimal price;
    private Double durationHours;
    private Byte status; //1 = PENDING, 2 = APPROVED, 3 = REJECTED
    private String rejectionReason;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<ManageMenuItemImagesResponse> menuItemImagesResponseList;
}

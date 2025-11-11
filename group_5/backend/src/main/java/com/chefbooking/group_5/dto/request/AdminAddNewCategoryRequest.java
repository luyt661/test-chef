package com.chefbooking.group_5.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminAddNewCategoryRequest {
    @NotBlank(message = "Category Name không được để trống")
    private String categoryName;
    private String description;
}

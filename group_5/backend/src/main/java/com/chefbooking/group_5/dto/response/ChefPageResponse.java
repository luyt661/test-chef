package com.chefbooking.group_5.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class ChefPageResponse {
    private List<ChefProfileCVResponse> users;

    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}

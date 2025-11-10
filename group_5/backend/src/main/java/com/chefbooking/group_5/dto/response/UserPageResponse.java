package com.chefbooking.group_5.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserPageResponse {
    private List<UserWithRolesResponse> users;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;

}

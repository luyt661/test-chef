package com.chefbooking.group_5.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOverviewResponse {

    // Tổng số user
    private Long totalAccounts;

    // User mới
    private Long newUsersLast7Days;
    private Long newChefsLast7Days;
    private Long newUsersLast30Days;
    private Long newChefsLast30Days;

    // Active / Inactive
    private Long activeAccounts;
    private Long usersAccountActive;
    private Long chefsAccountActive;
    private Long adminsAccountActive;
    private Long inactiveAccounts;
    private Long usersAccountInactive;
    private Long chefsAccountInactive;
    private Long adminsAccountInactive;

}


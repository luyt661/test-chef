package com.chefbooking.group_5.dto.request;

import lombok.Data;

@Data
public class AdminUpdateStatusMenuItemRequest {
    private Byte status;
    private  String rejectionReason;
}

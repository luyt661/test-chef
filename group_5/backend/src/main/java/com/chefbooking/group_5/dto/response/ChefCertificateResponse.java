package com.chefbooking.group_5.dto.response;

import java.time.LocalDate;

public interface ChefCertificateResponse {
    String getTitle();
    String getDescription();
    String getCertificateUrl();
    LocalDate getIssuedDate();
}

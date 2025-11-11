package com.chefbooking.group_5.repository;

import com.chefbooking.group_5.dto.response.ChefCertificateResponse;
import com.chefbooking.group_5.entity.ChefProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManageChefRepository extends JpaRepository<ChefProfile, Integer> {

    List<ChefProfile> findByStatus(Byte status);

    //Danh sách Ceritficate theo userId
    @Query(
    value = """
            SELECT title AS title,
                description AS description,
                certificate_url AS certificateUrl,
                issued_date AS issuedDate
            FROM Chef_Certificates
            WHERE chef_id = :chef_id;
            """,
    nativeQuery = true
    )
    List<ChefCertificateResponse> getChefCertificateResponse(@Param("chef_id") Integer chef_id);

}

package com.chefbooking.group_5.repository;

import com.chefbooking.group_5.dto.response.ManageMenuItemImagesResponse;
import com.chefbooking.group_5.entity.MenuItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManageMenuRepository extends JpaRepository<MenuItem, Integer> {

    // Lấy danh sách MenuItem theo chefId + status (status của MenuItem)
    @Query(
            value = """
                SELECT *
                FROM MenuItems
                WHERE chef_id = :chefId
                  AND status = :status
                """,
            nativeQuery = true
    )
    List<MenuItem> findMenuItemsByChefAndStatus(@Param("chefId") Integer chefId, @Param("status") Byte status);

    // Lấy danh sách ảnh theo menu_item_id (projection)
    @Query(value = """
            SELECT  image_id AS imageId,
                    menu_item_id AS menuItemId,
                    image_url AS imageUrl,
                    caption AS caption
            FROM MenuItem_Images
            WHERE menu_item_id = :menuItemId
            """,
            nativeQuery = true)
    List<ManageMenuItemImagesResponse> getMenuItemImageResponse(@Param("menuItemId") Integer menuItemId);

    @Query(value = "SELECT * " +
            "FROM MenuItems " +
            "WHERE menu_item_id = :menuItemId",
            nativeQuery = true)
    MenuItem findByMenuItemId(@Param("menuItemId") Integer menuItemId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE MenuItems
        SET 
            status = :status,
            rejection_reason = :rejectionReason,
            updated_at = GETDATE()
        WHERE menu_item_id = :menuItemId
    """, nativeQuery = true)
    int updateMenuItemStatusAndReasonNative(
            @Param("menuItemId") Integer menuItemId,
            @Param("status") Byte status,
            @Param("rejectionReason") String rejectionReason
    );

}

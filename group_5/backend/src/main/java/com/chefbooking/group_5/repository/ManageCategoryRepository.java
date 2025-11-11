package com.chefbooking.group_5.repository;

import com.chefbooking.group_5.dto.response.ManageCategoryResponse;
import com.chefbooking.group_5.dto.response.ManageMenuItemImagesResponse;
import com.chefbooking.group_5.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManageCategoryRepository extends JpaRepository<Category, Integer> {

    @Query(
            value = """
                SELECT
                    category_id AS categoryId,
                    category_name AS categoryName,
                    description AS description
                FROM Categories
                """,
            nativeQuery = true
    )
    List<ManageCategoryResponse> getAllCategories();

    //Kiểmt tra dữ liệu đã có trong databse hay chưa
    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM Category c
        WHERE REPLACE(LOWER(c.categoryName), ' ', '') = :normalizedName
    """)
    boolean existsBySimilarName(@Param("normalizedName") String normalizedName);

    @Query(
            value = """
            SELECT
                c.category_id AS categoryId,
                c.category_name AS categoryName,
                c.description AS description
            FROM Categories c
            WHERE LOWER(c.category_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """,
            nativeQuery = true
    )
    List<ManageCategoryResponse> findCategoriesByName(@Param("keyword") String keyword);

    @Query(
            value = """
            SELECT
                c.category_id AS categoryId,
                c.category_name AS categoryName,
                c.description AS description
            FROM Categories c
            WHERE category_id = :categoryId
            """,
            nativeQuery = true
    )
    ManageCategoryResponse getCategoryDetailByAdmin(@Param("categoryId") Integer categoryId);



}

package com.chefbooking.group_5.repository;

import com.chefbooking.group_5.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Tìm user theo email
    User findByEmail(String email);

    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    // Lấy user active + fetch luôn userRoles + role
    @Query("""
            SELECT DISTINCT u
            FROM User u
            LEFT JOIN FETCH u.userRoles ur
            LEFT JOIN FETCH ur.role
            WHERE u.isActive = true
            """)
    List<User> findActiveUsersWithRoles();

    @Query("""
            SELECT DISTINCT u
            FROM User u
            LEFT JOIN FETCH u.userRoles ur
            LEFT JOIN FETCH ur.role
            WHERE u.isActive = false
            """)
    List<User> findInactiveUsersWithRoles();

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE user_role
    SET role_id = :roleId
    WHERE user_id = :userId
    """, nativeQuery = true)
    void updateRoleByUserId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

}


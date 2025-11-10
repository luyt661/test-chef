package com.chefbooking.group_5.repository;

import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.entity.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Modifying
    @Transactional
    void deleteByUser(User user);
}
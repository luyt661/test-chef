package com.chefbooking.group_5.repository;

import com.chefbooking.group_5.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}
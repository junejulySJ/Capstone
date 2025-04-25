package com.capstone.meetingmap.userrole.repository;

import com.capstone.meetingmap.userrole.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    Optional<UserRole> findByUserType(Integer userType);
}

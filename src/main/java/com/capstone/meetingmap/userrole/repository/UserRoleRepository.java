package com.capstone.meetingmap.userrole.repository;

import com.capstone.meetingmap.userrole.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    Optional<UserRole> findByUserType(Integer userType);
    List<UserRole> findAll();
}

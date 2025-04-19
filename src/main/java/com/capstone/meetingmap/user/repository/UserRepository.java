package com.capstone.meetingmap.user.repository;

import com.capstone.meetingmap.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);
    List<User> findAll();
}

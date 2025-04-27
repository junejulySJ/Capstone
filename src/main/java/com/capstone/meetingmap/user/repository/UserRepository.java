package com.capstone.meetingmap.user.repository;

import com.capstone.meetingmap.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

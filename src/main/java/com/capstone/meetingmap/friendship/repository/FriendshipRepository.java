package com.capstone.meetingmap.friendship.repository;

import com.capstone.meetingmap.friendship.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    boolean existsByFriendshipNoAndUser_UserIdAndIsFrom(Integer friendshipNo, String userId, boolean isFrom);
}

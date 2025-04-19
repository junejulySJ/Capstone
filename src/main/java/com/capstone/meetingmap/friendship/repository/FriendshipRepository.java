package com.capstone.meetingmap.friendship.repository;

import com.capstone.meetingmap.friendship.entity.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository {
    Friendship save(Friendship friendship);
    Optional<Friendship> findByFriendshipId(Integer friendshipId);
    List<Friendship> findAllByUserId(String userId);
    List<Friendship> findAll();
}

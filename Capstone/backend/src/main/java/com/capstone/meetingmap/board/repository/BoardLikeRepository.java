package com.capstone.meetingmap.board.repository;

import com.capstone.meetingmap.board.entity.BoardInteractionId;
import com.capstone.meetingmap.board.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, BoardInteractionId> {
    boolean existsByBoard_BoardNoAndUser_UserId(Integer boardNo, String userId);
}

package com.capstone.meetingmap.board.repository;

import com.capstone.meetingmap.board.entity.BoardHate;
import com.capstone.meetingmap.board.entity.BoardInteractionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHateRepository extends JpaRepository<BoardHate, BoardInteractionId> {
    boolean existsByBoard_BoardNoAndUser_UserId(Integer boardNo, String userId);
}

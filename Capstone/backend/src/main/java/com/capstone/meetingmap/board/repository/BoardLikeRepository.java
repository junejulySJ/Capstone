package com.capstone.meetingmap.board.repository;

import com.capstone.meetingmap.board.entity.BoardInteractionId;
import com.capstone.meetingmap.board.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLike, BoardInteractionId> {
    boolean existsByBoard_BoardNoAndUser_UserId(Integer boardNo, String userId);

    @Query("SELECT bl.board.boardNo FROM BoardLike bl WHERE bl.user.userId = :userId")
    List<Integer> findBoardNosByUserId(@Param("userId") String userId);
}

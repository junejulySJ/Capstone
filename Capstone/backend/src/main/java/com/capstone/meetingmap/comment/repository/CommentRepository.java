package com.capstone.meetingmap.comment.repository;

import com.capstone.meetingmap.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    boolean existsByCommentNoAndUser_UserId(Integer commentNo, String userId);
    Optional<Comment> findByCommentNoAndUser_UserId(Integer commentNo, String userId);
    Page<Comment> findAllByBoard_BoardNo(Integer commentNo, Pageable pageable);
    void deleteByBoard_BoardNo(Integer boardNo);
}

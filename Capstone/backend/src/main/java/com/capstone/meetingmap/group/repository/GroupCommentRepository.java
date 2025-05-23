package com.capstone.meetingmap.group.repository;

import com.capstone.meetingmap.group.entity.GroupComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupCommentRepository extends JpaRepository<GroupComment, Integer> {
    boolean existsByGroupCommentNoAndUser_UserIdAndGroupBoardGroupBoardNo(Integer groupCommentNo, String userId, Integer groupBoardNo);
    Optional<GroupComment> findByGroupCommentNoAndUser_UserIdAndGroupBoard_GroupBoardNo(Integer groupCommentNo, String userId, Integer groupBoardNo);
    Page<GroupComment> findAllByGroupBoard_GroupBoardNo(Integer groupBoardNo, Pageable pageable);
}

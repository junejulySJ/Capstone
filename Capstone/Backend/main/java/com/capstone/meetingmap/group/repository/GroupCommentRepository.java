package com.capstone.meetingmap.group.repository;

import com.capstone.meetingmap.group.entity.GroupComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupCommentRepository extends JpaRepository<GroupComment, Integer> {
}

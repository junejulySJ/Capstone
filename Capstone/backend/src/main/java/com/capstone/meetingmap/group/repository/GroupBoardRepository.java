package com.capstone.meetingmap.group.repository;

import com.capstone.meetingmap.group.entity.GroupBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupBoardRepository extends JpaRepository<GroupBoard, Integer> {
    boolean existsByGroupBoardNoAndUser_UserId(Integer groupBoardNo, String userId);
    List<GroupBoard> findAllByGroup_GroupNoOrderByGroupBoardUpdateDateDesc(Integer groupNo);
}

package com.capstone.meetingmap.groupuser.repository;

import com.capstone.meetingmap.groupuser.entity.GroupUser;
import com.capstone.meetingmap.groupuser.entity.GroupUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUserId> {
    boolean existsBySchedule_ScheduleNoAndUser_UserId(Integer scheduleNo, String userId);
    List<GroupUser> findByUser_UserId(String userId);
    List<GroupUser> findBySchedule_ScheduleNo(Integer scheduleNo);
    Optional<GroupUser> findByUser_UserIdAndSchedule_ScheduleNo(String userId, Integer scheduleNo);
    void deleteByScheduleScheduleNo(Integer scheduleNo);
}
package com.capstone.meetingmap.group.repository;

import com.capstone.meetingmap.group.entity.GroupSchedule;
import com.capstone.meetingmap.group.entity.GroupScheduleId;
import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.schedule.entity.ScheduleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, GroupScheduleId> {
    boolean existsByGroup_GroupNoAndSchedule_ScheduleNo(Integer groupNo, Integer scheduleNo);

    @Query("SELECT gs.schedule FROM GroupSchedule gs WHERE gs.group.groupNo = :groupNo")
    List<Schedule> findSchedulesByGroup_GroupNo(Integer groupNo);

    @Query("SELECT sd FROM GroupSchedule gs " +
            "JOIN gs.schedule s " +
            "JOIN s.details sd " +
            "WHERE gs.group.groupNo = :groupNo AND s.scheduleNo = :scheduleNo")
    List<ScheduleDetail> findScheduleDetailsByGroup_GroupNoAndSchedule_ScheduleNo(
            @Param("groupNo") Integer groupNo,
            @Param("scheduleNo") Integer scheduleNo
    );

    void deleteByScheduleScheduleNo(Integer scheduleNo);
    void deleteByGroupGroupNoAndScheduleScheduleNo(Integer groupNo, Integer scheduleNo);
    long countByGroup_GroupNo(Integer groupNo);
}
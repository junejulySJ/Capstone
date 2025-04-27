package com.capstone.meetingmap.schedule.repository;

import com.capstone.meetingmap.schedule.entity.ScheduleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleDetailRepository extends JpaRepository<ScheduleDetail, Integer> {
    void deleteByScheduleScheduleNo(Integer scheduleNo);
    List<ScheduleDetail> findBySchedule_ScheduleNo(Integer scheduleNo);
}

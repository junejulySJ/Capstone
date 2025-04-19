package com.capstone.meetingmap.schedule.repository;

import com.capstone.meetingmap.schedule.entity.ScheduleDetail;

import java.util.List;

public interface ScheduleDetailRepository {
    ScheduleDetail save(ScheduleDetail scheduleDetail);
    void deleteByScheduleNo(Integer scheduleNo);
    List<ScheduleDetail> findAllByScheduleNo(Integer scheduleNo);
}

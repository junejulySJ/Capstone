package com.capstone.meetingmap.schedule.repository;

import com.capstone.meetingmap.schedule.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);
    Schedule update(Schedule schedule);
    void deleteByScheduleNo(Integer scheduleNo);
    Optional<Schedule> findByScheduleNo(Integer scheduleNo);
    List<Schedule> findAll();
}

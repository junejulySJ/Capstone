package com.capstone.meetingmap.schedule.repository;

import com.capstone.meetingmap.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    boolean existsByScheduleNoAndUser_UserId(Integer scheduleNo, String userId);
}

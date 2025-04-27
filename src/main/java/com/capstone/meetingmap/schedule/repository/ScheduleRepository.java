package com.capstone.meetingmap.schedule.repository;

import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    @Query("SELECT s FROM Schedule s JOIN GroupUser gu ON s.scheduleNo = gu.schedule.scheduleNo WHERE gu.user.userId = :userId")
    List<Schedule> findSchedulesByUserId(@Param("userId") String userId);

    String user(User user);
}

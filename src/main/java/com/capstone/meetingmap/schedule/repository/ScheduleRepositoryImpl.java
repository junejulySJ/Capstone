package com.capstone.meetingmap.schedule.repository;

import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.user.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final EntityManager em;

    public ScheduleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Schedule save(Schedule schedule) { //기존 mapper의 insertSchedule(schedule)에 호환
        em.persist(schedule);
        return schedule;
    }

    @Override
    public Schedule update(Schedule schedule) {
        return em.merge(schedule);
    }


    @Override
    public void deleteByScheduleNo(Integer scheduleNo) { //기존 mapper의 deleteSchedule(scheduleNo)에 호환
        Schedule schedule = em.find(Schedule.class, scheduleNo);
        if (schedule != null) {
            em.remove(schedule);
        }
    }

    @Override
    public Optional<Schedule> findByScheduleNo(Integer scheduleNo) {
        Schedule schedule = em.find(Schedule.class, scheduleNo);
        return Optional.ofNullable(schedule);
    }

    @Override
    public List<Schedule> findAll() { //기존 mapper의 selectAllSchedules()에 호환
        return em.createQuery("select s from Schedule s", Schedule.class)
                .getResultList();
    }
}

package com.capstone.meetingmap.schedule.repository;

import com.capstone.meetingmap.schedule.entity.ScheduleDetail;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ScheduleDetailRepositoryImpl implements ScheduleDetailRepository {

    private final EntityManager em;

    public ScheduleDetailRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public ScheduleDetail save(ScheduleDetail scheduleDetail) { // 기존 mapper의 insertScheduleDetail(detail)에 호환
        em.persist(scheduleDetail);
        return scheduleDetail;
    }

    @Override
    public void deleteByScheduleNo(Integer scheduleNo) { // 기존 mapper의 deleteScheduleDetails(scheduleNo)에 호환
        ScheduleDetail scheduleDetail = em.find(ScheduleDetail.class, scheduleNo);
        if (scheduleDetail != null) {
            em.remove(scheduleDetail);
        }
    }

    @Override
    public List<ScheduleDetail> findAllByScheduleNo(Integer scheduleNo) { // 기존 mapper의 selectScheduleDetails(scheduleNo)에 호환
        return em.createQuery("select s from ScheduleDetail s where s.schedule.scheduleNo = :scheduleNo", ScheduleDetail.class)
                .setParameter("scheduleNo", scheduleNo)
                .getResultList();
    }
}

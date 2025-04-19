package com.capstone.meetingmap.schedule.service;

import com.capstone.meetingmap.schedule.dto.*;
import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.schedule.entity.ScheduleDetail;
import com.capstone.meetingmap.schedule.repository.ScheduleDetailRepository;
import com.capstone.meetingmap.schedule.repository.ScheduleRepository;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final UserRepository userRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleDetailRepository scheduleDetailRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleDetailRepository = scheduleDetailRepository;
        this.userRepository = userRepository;
    }

    public List<ScheduleResponseDto> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleResponseDto> scheduleDtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDtos.add(ScheduleResponseDto.fromEntity(schedule));
        }
        return scheduleDtos;
    }

    public List<ScheduleDetailResponseDto> getScheduleDetails(int scheduleNo) {
        List<ScheduleDetail> scheduleDetails = scheduleDetailRepository.findAllByScheduleNo(scheduleNo);
        List<ScheduleDetailResponseDto> scheduleDetailDtos = new ArrayList<>();
        for (ScheduleDetail scheduleDetail : scheduleDetails) {
            scheduleDetailDtos.add(ScheduleDetailResponseDto.fromEntity(scheduleDetail));
        }
        return scheduleDetailDtos;
    }


    @Transactional
    public void createScheduleWithDetails(ScheduleCreateRequestDto scheduleCreateRequestDto) {
        // 1. User 조회
        User user = userRepository.findByUserId(scheduleCreateRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Schedule 생성 및 저장
        Schedule schedule = scheduleCreateRequestDto.toEntity(scheduleCreateRequestDto, user);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // 3. ScheduleDetail 저장
        for (ScheduleDetailRequestDto detailDto : scheduleCreateRequestDto.getDetails()) {
            ScheduleDetail detail = detailDto.toEntity(savedSchedule);
            scheduleDetailRepository.save(detail);
        }
    }

    @Transactional
    public void updateSchedule(Integer scheduleNo, ScheduleUpdaterRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findByScheduleNo(scheduleNo)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        scheduleDetailRepository.deleteByScheduleNo(scheduleNo); // 기존 상세일정 삭제

        //일정 저장
        schedule.setScheduleWithoutUserId(requestDto.getScheduleName(), requestDto.getScheduleAbout());
        scheduleRepository.save(schedule);

        //디테일 저장
        for (ScheduleDetailRequestDto detailDto : requestDto.getDetails()) {
            ScheduleDetail detail = detailDto.toEntity(schedule);
            scheduleDetailRepository.save(detail);
        }
    }

    @Transactional
    public void deleteSchedule(Integer scheduleNo) {
        scheduleRepository.deleteByScheduleNo(scheduleNo);
    }
















}

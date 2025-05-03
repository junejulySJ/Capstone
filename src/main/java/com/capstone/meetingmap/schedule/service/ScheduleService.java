package com.capstone.meetingmap.schedule.service;

import com.capstone.meetingmap.groupuser.entity.GroupUser;
import com.capstone.meetingmap.groupuser.repository.GroupUserRepository;
import com.capstone.meetingmap.schedule.dto.*;
import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.schedule.entity.ScheduleDetail;
import com.capstone.meetingmap.schedule.repository.ScheduleDetailRepository;
import com.capstone.meetingmap.schedule.repository.ScheduleRepository;
import com.capstone.meetingmap.user.dto.UserResponseDto;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final UserRepository userRepository;
    private final GroupUserRepository groupUserRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleDetailRepository scheduleDetailRepository, UserRepository userRepository, GroupUserRepository groupUserRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleDetailRepository = scheduleDetailRepository;
        this.userRepository = userRepository;
        this.groupUserRepository = groupUserRepository;
    }

    // 회원이 속한 모든 스케줄 가져오기
    public List<ScheduleResponseDto> getSchedulesByUserId(String userId) {
        // userId로 해당되는 그룹 데이터 가져오기
        List<GroupUser> groupUsers = groupUserRepository.findByUser_UserId(userId);

        // Set으로 중복 자동 제거
        Set<Schedule> schedules = groupUsers.stream()
                .map(GroupUser::getSchedule)
                .collect(Collectors.toSet());

        // 스케줄 목록 반환
        return schedules.stream()
                .map(ScheduleResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 스케줄 세부 정보 가져오기
    public List<ScheduleDetailResponseDto> getScheduleDetails(Integer scheduleNo) {
        List<ScheduleDetail> scheduleDetails = scheduleDetailRepository.findBySchedule_ScheduleNo(scheduleNo);
        List<ScheduleDetailResponseDto> responseDtoList = new ArrayList<>();
        for (ScheduleDetail scheduleDetail : scheduleDetails) {
            responseDtoList.add(ScheduleDetailResponseDto.fromEntity(scheduleDetail));
        }
        return responseDtoList;
    }

    // 스케줄에 속한 모든 구성원 가져오기
    public List<UserResponseDto> getUsersByScheduleNo(Integer scheduleNo) {
        List<GroupUser> groupUsers = groupUserRepository.findBySchedule_ScheduleNo(scheduleNo);
        return groupUsers.stream()
                .map(groupUser -> UserResponseDto.fromEntity(groupUser.getUser()))
                .collect(Collectors.toList());
    }

    // 세부 스케줄과 함께 스케줄 추가
    @Transactional
    public Integer createScheduleWithDetails(ScheduleCreateRequestDto scheduleCreateRequestDto) {
        // 1. User 조회
        User user = userRepository.findById(scheduleCreateRequestDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

        // 2. Schedule 생성 및 저장
        Schedule schedule = scheduleCreateRequestDto.toEntity(scheduleCreateRequestDto, user);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // 3. ScheduleDetail 저장
        for (ScheduleDetailRequestDto detailDto : scheduleCreateRequestDto.getDetails()) {
            ScheduleDetail detail = detailDto.toEntity(savedSchedule);
            scheduleDetailRepository.save(detail);
        }

        return savedSchedule.getScheduleNo();
    }

    // 스케줄 수정
    @Transactional
    public void updateSchedule(String userId, Integer scheduleNo, ScheduleUpdateRequestDto requestDto) {

        groupUserRepository.findByUser_UserId(userId);

        // userId와 scheduleNo로 GroupUser 검색 (해당 유저가 참여 중인 스케줄인지 확인)
        GroupUser groupUser = groupUserRepository.findByUser_UserIdAndSchedule_ScheduleNo(userId, scheduleNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 스케줄에 대한 참여 정보를 찾을 수 없습니다."));

        Schedule schedule = groupUser.getSchedule();

        scheduleDetailRepository.deleteByScheduleScheduleNo(scheduleNo); // 기존 상세일정 삭제

        //일정 저장
        schedule.setScheduleWithoutUserId(requestDto.getScheduleName(), requestDto.getScheduleAbout());
        scheduleRepository.save(schedule);

        //디테일 저장
        for (ScheduleDetailRequestDto detailDto : requestDto.getDetails()) {
            ScheduleDetail detail = detailDto.toEntity(schedule);
            scheduleDetailRepository.save(detail);
        }
    }

    // 스케줄 삭제
    @Transactional
    public void deleteSchedule(String userId, Integer scheduleNo) {

        // 자신이 만든 schedule만 삭제 가능하도록 변경하기
        if (!scheduleRepository.existsByScheduleNoAndUser_UserId(scheduleNo, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 만든 스케줄만 삭제 가능합니다");

        scheduleDetailRepository.deleteByScheduleScheduleNo(scheduleNo);
        scheduleRepository.deleteById(scheduleNo);
    }
}
package com.capstone.meetingmap.groupuser.service;

import com.capstone.meetingmap.groupuser.dto.GroupUserRequestDto;
import com.capstone.meetingmap.groupuser.entity.GroupUser;
import com.capstone.meetingmap.groupuser.repository.GroupUserRepository;
import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.schedule.repository.ScheduleRepository;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GroupUserService {
    private final GroupUserRepository groupUserRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public GroupUserService(GroupUserRepository groupUserRepository, ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.groupUserRepository = groupUserRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    // 그룹 생성
    public void addGroup(GroupUserRequestDto groupUserRequestDto) {
        // 해당 Schedule 검색
        Schedule schedule = scheduleRepository.findById(groupUserRequestDto.getScheduleNo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 스케줄을 찾을 수 없습니다"));

        // 해당 User 검색
        User user = userRepository.findById(groupUserRequestDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

        // Schedule과 User 정보로 엔티티 생성
        GroupUser groupUser = groupUserRequestDto.toEntity(schedule, user);

        // 엔티티를 db에 저장
        groupUserRepository.save(groupUser);
    }

    // 그룹 삭제
    public void deleteGroup(GroupUserRequestDto groupUserRequestDto) {

        // 자신이 속한 group만 삭제 가능하도록 변경하기
        if (!groupUserRepository.existsBySchedule_ScheduleNoAndUser_UserId(groupUserRequestDto.getScheduleNo(), groupUserRequestDto.getUserId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 속한 그룹만 삭제 가능합니다");

        groupUserRepository.deleteByScheduleScheduleNo(groupUserRequestDto.getScheduleNo());
    }
}
package com.capstone.meetingmap.groupuser.service;

import com.capstone.meetingmap.groupuser.entity.GroupUser;
import com.capstone.meetingmap.groupuser.repository.GroupUserRepository;
import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.schedule.repository.ScheduleRepository;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    // 1명 그룹 생성(추가)
    @Transactional
    public void addGroup(Integer scheduleNo, String userId) {
        addGroups(scheduleNo, List.of(userId));
    }

    // 여러명 그룹 생성(추가)
    @Transactional
    public void addGroups(Integer scheduleNo, List<String> userIds) {
        // 해당 Schedule 검색
        Schedule schedule = scheduleRepository.findById(scheduleNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 스케줄을 찾을 수 없습니다"));

        for (String userId : userIds) {
            // 해당 User 검색
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));
            // Schedule과 User 정보로 엔티티 생성
            GroupUser groupUser = GroupUser.builder()
                    .schedule(schedule)
                    .user(user)
                    .build();

            // 엔티티를 db에 저장
            groupUserRepository.save(groupUser);
        }
    }

    // 그룹 삭제
    @Transactional
    public void deleteGroup(Integer scheduleNo, String userId) {

        // 자신이 속한 group만 삭제 가능
        if (!groupUserRepository.existsBySchedule_ScheduleNoAndUser_UserId(scheduleNo, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 속한 그룹만 삭제 가능합니다");

        groupUserRepository.deleteByGroupUserId_ScheduleNo(scheduleNo);
    }


}
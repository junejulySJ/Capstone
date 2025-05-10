package com.capstone.meetingmap.schedule.controller;

import com.capstone.meetingmap.groupuser.service.GroupUserService;
import com.capstone.meetingmap.schedule.dto.*;
import com.capstone.meetingmap.schedule.service.ScheduleService;
import com.capstone.meetingmap.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final GroupUserService groupUserService;

    public ScheduleController(ScheduleService scheduleService, GroupUserService groupUserService) {
        this.scheduleService = scheduleService;
        this.groupUserService = groupUserService;
    }

    // 스케줄 조회
    @GetMapping
    public List<ScheduleResponseDto> getAllSchedules() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return scheduleService.getSchedulesByUserId(userId);
    }

    // 상세 정보 조회
    @GetMapping("/{scheduleNo}/details")
    public List<ScheduleDetailResponseDto> getScheduleDetails(@PathVariable Integer scheduleNo) {
        return scheduleService.getScheduleDetails(scheduleNo);
    }

    // 등록
    @PostMapping
    public ResponseEntity<?> saveSchedule(@RequestBody ScheduleSaveRequestDto request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer scheduleNo = scheduleService.saveScheduleWithDetails(userId, request);
        groupUserService.addGroup(scheduleNo, userId);
        return ResponseEntity.ok("일정 등록 성공");
    }

    // 삭제
    @DeleteMapping("/{scheduleNo}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Integer scheduleNo) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        groupUserService.deleteGroup(scheduleNo, userId);
        scheduleService.deleteSchedule(userId, scheduleNo);
        return ResponseEntity.ok("일정 삭제 성공");
    }

    // 수정
    @PutMapping("/{scheduleNo}")
    public ResponseEntity<?> updateSchedule(@PathVariable Integer scheduleNo, @RequestBody ScheduleUpdateRequestDto request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        scheduleService.updateSchedule(userId, scheduleNo, request);
        return ResponseEntity.ok("일정 수정 성공");
    }

    // 사용자로부터 입력받은 데이터를 통해 스케줄 생성
    @PostMapping("/create")
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        ScheduleCreateResponseDto scheduleCreateResponseDto = scheduleService.createSchedule(scheduleCreateRequestDto);
        return ResponseEntity.ok(scheduleCreateResponseDto);
    }

    // 특정 스케줄에 참여한 사용자 전체 목록 조회
    @GetMapping("/{scheduleNo}/members")
    public ResponseEntity<List<UserResponseDto>> getScheduleMembers(@PathVariable Integer scheduleNo) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserResponseDto> members = scheduleService.getUsersByScheduleNo(userId, scheduleNo);
        return ResponseEntity.ok(members);
    }

    // 스케줄 공유 요청
    @PostMapping("/share")
    public ResponseEntity<String> shareSchedule(@RequestBody ScheduleShareRequestDto request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        scheduleService.shareSchedule(userId, request);
        return ResponseEntity.ok("스케줄 공유 완료");
    }

    // 스케줄 공유 취소
    @PostMapping("/unshare")
    public ResponseEntity<String> unshareSchedule(@RequestBody ScheduleShareRequestDto request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        scheduleService.unshareSchedule(userId, request);
        return ResponseEntity.ok("스케줄 공유 취소 완료");
    }
}

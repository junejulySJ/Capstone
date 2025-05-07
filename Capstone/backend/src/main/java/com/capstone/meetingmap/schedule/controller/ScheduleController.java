package com.capstone.meetingmap.schedule.controller;

import com.capstone.meetingmap.groupuser.dto.GroupUserRequestDto;
import com.capstone.meetingmap.groupuser.service.GroupUserService;
import com.capstone.meetingmap.schedule.dto.*;
import com.capstone.meetingmap.schedule.service.ScheduleService;
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
    public ResponseEntity<String> saveSchedule(@RequestBody ScheduleSaveRequestDto request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer scheduleNo = scheduleService.saveScheduleWithDetails(userId, request);
        groupUserService.addGroup(new GroupUserRequestDto(scheduleNo, userId));
        return ResponseEntity.ok("일정 등록 성공");
    }

    // 삭제
    @DeleteMapping("/{scheduleNo}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Integer scheduleNo) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        groupUserService.deleteGroup(new GroupUserRequestDto(scheduleNo, userId));
        scheduleService.deleteSchedule(userId, scheduleNo);
        return ResponseEntity.ok().build();
    }

    // 수정
    @PutMapping("/{scheduleNo}")
    public ResponseEntity<Void> updateSchedule(@PathVariable Integer scheduleNo, @RequestBody ScheduleUpdateRequestDto request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        scheduleService.updateSchedule(userId, scheduleNo, request);
        return ResponseEntity.ok().build();
    }

    // 사용자로부터 입력받은 데이터를 통해 스케줄 생성
    @PostMapping("/create")
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        ScheduleCreateResponseDto scheduleCreateResponseDto = scheduleService.createSchedule(scheduleCreateRequestDto);
        return ResponseEntity.ok(scheduleCreateResponseDto);
    }
}

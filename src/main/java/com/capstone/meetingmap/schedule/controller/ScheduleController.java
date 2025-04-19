package com.capstone.meetingmap.schedule.controller;

import com.capstone.meetingmap.schedule.dto.ScheduleDetailResponseDto;
import com.capstone.meetingmap.schedule.dto.ScheduleCreateRequestDto;
import com.capstone.meetingmap.schedule.dto.ScheduleResponseDto;
import com.capstone.meetingmap.schedule.dto.ScheduleUpdaterRequestDto;
import com.capstone.meetingmap.schedule.service.ScheduleService;
import com.capstone.meetingmap.util.SessionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules() {
        System.out.println(SessionUtil.getLoggedInUserId());
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/schedules/{scheduleNo}/details")
    public List<ScheduleDetailResponseDto> getScheduleDetails(@PathVariable Integer scheduleNo) {
        return scheduleService.getScheduleDetails(scheduleNo);
    }

    @PostMapping("/schedules")
    public ResponseEntity<String> createSchedule(@RequestBody ScheduleCreateRequestDto request) {
        try {
            scheduleService.createScheduleWithDetails(request);
            return ResponseEntity.ok("일정 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류");
        }
    }

    // 삭제
    @DeleteMapping("/schedules/{scheduleNo}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Integer scheduleNo) {
        scheduleService.deleteSchedule(scheduleNo);
        return ResponseEntity.ok().build();
    }


    // 수정
    @PutMapping("/schedules/{scheduleNo}")
    public ResponseEntity<Void> updateSchedule(@PathVariable Integer scheduleNo, @RequestBody ScheduleUpdaterRequestDto request) {
        scheduleService.updateSchedule(scheduleNo, request);
        return ResponseEntity.ok().build();
    }

}

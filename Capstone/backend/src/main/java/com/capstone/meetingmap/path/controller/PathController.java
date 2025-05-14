package com.capstone.meetingmap.path.controller;

import com.capstone.meetingmap.path.service.PathService;
import com.capstone.meetingmap.schedule.dto.ScheduleDetailCreateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/path")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    // 보행자 길찾기
    @PostMapping("/pedestrian")
    public ResponseEntity<?> pedestrianPath(@RequestBody List<ScheduleDetailCreateDto> dtoList) {
        return ResponseEntity.ok(pathService.getPedestrianPath(dtoList));
    }

    // 자동차 길찾기
    @PostMapping("/car")
    public ResponseEntity<?> carPath(@RequestBody List<ScheduleDetailCreateDto> dtoList) {
        return ResponseEntity.ok(pathService.getCarPath(dtoList));
    }

    // 대중교통 길찾기
    @PostMapping("/transit")
    public ResponseEntity<?> transitPath(@RequestBody List<ScheduleDetailCreateDto> dtoList) {
        return ResponseEntity.ok(pathService.getTransitPath(dtoList));
    }
}

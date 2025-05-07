package com.capstone.meetingmap.path.controller;

import com.capstone.meetingmap.api.kakaomobility.service.KakaoMobilityDirectionService;
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
    private final KakaoMobilityDirectionService kakaoMobilityDirectionService;

    public PathController(KakaoMobilityDirectionService kakaoMobilityDirectionService) {
        this.kakaoMobilityDirectionService = kakaoMobilityDirectionService;
    }

    @PostMapping
    public ResponseEntity<?> path(@RequestBody List<ScheduleDetailCreateDto> dtoList) {
        System.out.println(dtoList);
        return ResponseEntity.ok(kakaoMobilityDirectionService.getPath(dtoList));
    }
}

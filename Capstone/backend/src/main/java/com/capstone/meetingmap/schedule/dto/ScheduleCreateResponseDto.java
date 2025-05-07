package com.capstone.meetingmap.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleCreateResponseDto {
    private String scheduleName;
    private String scheduleAbout;
    private List<ScheduleDetailCreateDto> details;

    @Builder
    public ScheduleCreateResponseDto(String scheduleName, String scheduleAbout, List<ScheduleDetailCreateDto> details) {
        this.scheduleName = scheduleName;
        this.scheduleAbout = scheduleAbout;
        this.details = details;
    }
}

package com.capstone.meetingmap.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleUpdateRequestDto {
    private Integer scheduleNo;
    private String scheduleName;
    private String scheduleAbout;
    private List<ScheduleDetailRequestDto> details;
}

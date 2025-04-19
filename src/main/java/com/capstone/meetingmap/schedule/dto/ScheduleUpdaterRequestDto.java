package com.capstone.meetingmap.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleUpdaterRequestDto {
    private Integer scheduleNo;
    private String scheduleName;
    private String scheduleAbout;
    private List<ScheduleDetailRequestDto> details;

    @Builder
    public ScheduleUpdaterRequestDto(Integer scheduleNo, String scheduleName, String scheduleAbout, List<ScheduleDetailRequestDto> details) {
        this.scheduleNo = scheduleNo;
        this.scheduleName = scheduleName;
        this.scheduleAbout = scheduleAbout;
        this.details = details;
    }

}

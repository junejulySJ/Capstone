package com.capstone.meetingmap.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleDetailCreateDto {
    private String scheduleContent;
    private String scheduleAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime scheduleStartTime;
    private LocalDateTime scheduleEndTime;

    @Builder
    public ScheduleDetailCreateDto(String scheduleContent, String scheduleAddress, BigDecimal latitude, BigDecimal longitude, LocalDateTime scheduleStartTime, LocalDateTime scheduleEndTime) {
        this.scheduleContent = scheduleContent;
        this.scheduleAddress = scheduleAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.scheduleStartTime = scheduleStartTime;
        this.scheduleEndTime = scheduleEndTime;
    }
}

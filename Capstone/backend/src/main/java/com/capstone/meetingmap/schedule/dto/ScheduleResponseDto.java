package com.capstone.meetingmap.schedule.dto;

import com.capstone.meetingmap.schedule.entity.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleResponseDto {
    private Integer scheduleNo;
    private String scheduleName;
    private String scheduleAbout;
    private LocalDateTime scheduleCreatedDate;
    private String userId;

    @Builder
    public ScheduleResponseDto(Integer scheduleNo, String scheduleName, String scheduleAbout, LocalDateTime scheduleCreatedDate, String userId) {
        this.scheduleNo = scheduleNo;
        this.scheduleName = scheduleName;
        this.scheduleAbout = scheduleAbout;
        this.scheduleCreatedDate = scheduleCreatedDate;
        this.userId = userId;
    }

    //엔티티를 dto로 변환
    public static ScheduleResponseDto fromEntity(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .scheduleNo(schedule.getScheduleNo())
                .scheduleName(schedule.getScheduleName())
                .scheduleAbout(schedule.getScheduleAbout())
                .scheduleCreatedDate(schedule.getScheduleCreatedDate())
                .userId(schedule.getUser().getUserId())
                .build();
    }
}

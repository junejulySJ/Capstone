package com.capstone.meetingmap.schedule.dto;

import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequestDto {
    private String userId;
    private String scheduleName;
    private String scheduleAbout;
    private List<ScheduleDetailRequestDto> details;

    @Builder
    public ScheduleCreateRequestDto(String userId, String scheduleName, String scheduleAbout, List<ScheduleDetailRequestDto> details) {
        this.userId = userId;
        this.scheduleName = scheduleName;
        this.scheduleAbout = scheduleAbout;
        this.details = details;
    }

    //dto를 엔티티로 변환
    public Schedule toEntity(ScheduleCreateRequestDto scheduleCreateRequestDto, User user) {
        return Schedule.builder()
                .scheduleName(scheduleCreateRequestDto.getScheduleName())
                .scheduleAbout(scheduleCreateRequestDto.getScheduleAbout())
                .user(user)
                .build();
    }
}

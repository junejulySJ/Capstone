package com.capstone.meetingmap.schedule.dto;

import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleSaveRequestDto {
    private String scheduleName;
    private String scheduleAbout;
    private List<ScheduleDetailRequestDto> details;

    @Builder
    public ScheduleSaveRequestDto(String scheduleName, String scheduleAbout, List<ScheduleDetailRequestDto> details) {
        this.scheduleName = scheduleName;
        this.scheduleAbout = scheduleAbout;
        this.details = details;
    }

    //dto를 엔티티로 변환
    public Schedule toEntity(ScheduleSaveRequestDto scheduleSaveRequestDto, User user) {
        return Schedule.builder()
                .scheduleName(scheduleSaveRequestDto.getScheduleName())
                .scheduleAbout(scheduleSaveRequestDto.getScheduleAbout())
                .user(user)
                .build();
    }
}

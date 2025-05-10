package com.capstone.meetingmap.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleShareRequestDto {
    private Integer scheduleNo;       // 공유할 스케줄 번호
    private List<String> userIds;   // 공유할 친구 ID 목록

    @Builder
    public ScheduleShareRequestDto(Integer scheduleNo, List<String> userIds) {
        this.scheduleNo = scheduleNo;
        this.userIds = userIds;
    }
}

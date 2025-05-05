package com.capstone.meetingmap.groupuser.dto;

import com.capstone.meetingmap.groupuser.entity.GroupUser;
import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupUserRequestDto {
    private Integer scheduleNo;
    private String userId;

    @Builder
    public GroupUserRequestDto(Integer scheduleNo, String userId) {
        this.scheduleNo = scheduleNo;
        this.userId = userId;
    }

    //dto를 엔티티로 변환
    public GroupUser toEntity(Schedule schedule, User user) {
        return GroupUser.builder()
                .schedule(schedule)
                .user(user)
                .build();
    }
}

package com.capstone.meetingmap.groupuser.entity;

import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GroupUser {

    @EmbeddedId
    private GroupUserId groupUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scheduleNo")  // 복합키 클래스(GroupUserId)의 scheduleNo와 연결
    @JoinColumn(name = "schedule_no", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")  // 복합키 클래스(GroupUserId)의 userId와 연결
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public GroupUser(Schedule schedule, User user) {
        this.groupUserId = new GroupUserId(schedule.getScheduleNo(), user.getUserId());
        this.schedule = schedule;
        this.user = user;
    }
}

package com.capstone.meetingmap.groupuser.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
//복합키 구현을 위한 별도의 PK 클래스
public class GroupUserId implements Serializable {
    private Integer scheduleNo;
    private String userId;

    public GroupUserId(Integer scheduleNo, String userId) {
        this.scheduleNo = scheduleNo;
        this.userId = userId;
    }
}

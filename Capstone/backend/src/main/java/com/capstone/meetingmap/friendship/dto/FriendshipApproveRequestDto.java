package com.capstone.meetingmap.friendship.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendshipApproveRequestDto {
    private Integer friendshipNo;

    @Builder
    public FriendshipApproveRequestDto(Integer friendshipNo) {
        this.friendshipNo = friendshipNo;
    }
}

package com.capstone.meetingmap.friendship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FriendshipApproveRequestDto {
    private Integer friendshipNo;
}

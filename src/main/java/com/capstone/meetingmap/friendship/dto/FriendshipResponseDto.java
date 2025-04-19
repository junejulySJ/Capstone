package com.capstone.meetingmap.friendship.dto;

import com.capstone.meetingmap.friendship.entity.Friendship;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendshipResponseDto {
    private Integer friendshipNo;
    private String userId;
    private String opponentId;
    private String opponentNick;
    private String status;
    private boolean isFrom;
    private Integer counterpartFriendshipNo;

    @Builder
    public FriendshipResponseDto(Integer friendshipNo, String userId, String opponentId, String opponentNick, String status, boolean isFrom, Integer counterpartFriendshipNo) {
        this.friendshipNo = friendshipNo;
        this.userId = userId;
        this.opponentId = opponentId;
        this.opponentNick = opponentNick;
        this.status = status;
        this.isFrom = isFrom;
        this.counterpartFriendshipNo = counterpartFriendshipNo;
    }

    //엔티티를 dto로 변환
    public static FriendshipResponseDto fromEntity(Friendship friendship) {
        return FriendshipResponseDto.builder()
                .friendshipNo(friendship.getFriendshipNo())
                .userId(friendship.getUser().getUserId())
                .opponentId(friendship.getOpponent().getUserId())
                .opponentNick(friendship.getOpponent().getUserNick())
                .status(friendship.getStatus().name())
                .isFrom(friendship.isFrom())
                .counterpartFriendshipNo(friendship.getCounterpartFriendshipNo())
                .build();
    }
}

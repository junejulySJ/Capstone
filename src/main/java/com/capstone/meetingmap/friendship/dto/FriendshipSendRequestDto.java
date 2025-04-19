package com.capstone.meetingmap.friendship.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendshipSendRequestDto {
    @NotBlank(message = "추가할 친구 id는 반드시 입력해야 합니다.")
    private String opponentId; //세션 로그인 사용중이므로 나의 id는 보내지 않음

    @Builder
    public FriendshipSendRequestDto(String opponentId) {
        this.opponentId = opponentId;
    }
}

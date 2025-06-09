package com.capstone.meetingmap.friendship.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FriendshipAddRequestDto {
    @NotBlank(message = "추가할 친구 이름은 반드시 입력해야 합니다.")
    private String opponentNick;

    @NotBlank(message = "추가할 친구 이메일은 반드시 입력해야 합니다.")
    private String opponentEmail;
}

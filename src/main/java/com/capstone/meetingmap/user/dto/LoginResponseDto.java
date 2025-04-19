package com.capstone.meetingmap.user.dto;

import com.capstone.meetingmap.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private String userId;
    private String userNick;
    private String message;

    @Builder
    public LoginResponseDto(String userId, String userNick, String message) {
        this.userId = userId;
        this.userNick = userNick;
        this.message = message;
    }

    //엔티티를 dto로 변환
    public static LoginResponseDto fromEntity(User user, String message) {
        return LoginResponseDto.builder()
                .userId(user.getUserId())
                .userNick(user.getUserNick())
                .message(message)
                .build();
    }
}

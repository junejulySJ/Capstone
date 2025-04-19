package com.capstone.meetingmap.user.dto;

import com.capstone.meetingmap.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterResponseDto {
    private String userId;

    @Builder
    public UserRegisterResponseDto(String userId) {
        this.userId = userId;
    }

    //엔티티를 dto로 변환
    public static UserRegisterResponseDto fromEntity(User user) {
        return UserRegisterResponseDto.builder()
                .userId(user.getUserId())
                .build();
    }
}

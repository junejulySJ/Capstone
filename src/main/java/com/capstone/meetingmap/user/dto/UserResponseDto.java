package com.capstone.meetingmap.user.dto;

import com.capstone.meetingmap.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String userId;
    private String userEmail;
    private String userNick;
    private String userImg;
    private String userAddress;
    private Integer userType;

    @Builder
    public UserResponseDto(String userId, String userEmail, String userNick, String userImg, String userAddress, Integer userType) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userNick = userNick;
        this.userImg = userImg;
        this.userAddress = userAddress;
        this.userType = userType;
    }

    //엔티티를 dto로 변환
    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userNick(user.getUserNick())
                .userAddress(user.getUserAddress())
                .userType(user.getUserRole().getUserType())
                .build();
    }
}

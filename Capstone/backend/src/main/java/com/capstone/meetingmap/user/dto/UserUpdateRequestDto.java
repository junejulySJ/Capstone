package com.capstone.meetingmap.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    private String userEmail;
    private String userNick;
    private String userAddress;

    @Builder
    public UserUpdateRequestDto(String userEmail, String userNick, String userAddress) {
        this.userEmail = userEmail;
        this.userNick = userNick;
        this.userAddress = userAddress;
    }
}

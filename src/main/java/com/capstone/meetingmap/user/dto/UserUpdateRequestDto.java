package com.capstone.meetingmap.user.dto;

import com.capstone.meetingmap.user.entity.User;
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

    //dto를 엔티티로 변환
    public User toEntity(User user) {
        return User.builder()
                .userId(user.getUserId())
                .userEmail(this.userEmail != null ? this.userEmail : user.getUserEmail())
                .userPasswd(user.getUserPasswd())
                .userNick(this.userNick != null ? this.userNick : user.getUserNick())
                .userAddress(this.userAddress != null ? this.userAddress : user.getUserAddress())
                .userRole(user.getUserRole())
                .build();
    }
}

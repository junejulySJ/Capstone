package com.capstone.meetingmap.group.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupInvitationRequestDto {
    @NotBlank(message = "초대받을 사용자 ID는 반드시 입력해야 합니다")
    private String userId;
}

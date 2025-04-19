package com.capstone.meetingmap.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//프론트에 areaCode를 전달할 dto
@Getter
@NoArgsConstructor
public class CodeResponseDto {
    private String code;
    private String name;

    @Builder
    public CodeResponseDto(String code, String name) {
        this.code = code;
        this.name = name;
    }
}

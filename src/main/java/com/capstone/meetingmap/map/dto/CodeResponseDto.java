package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.map.dto.tourapi.CodeItem;
import com.capstone.meetingmap.map.entity.ContentType;
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

    public static CodeResponseDto fromCodeItem(CodeItem item) {
        return CodeResponseDto.builder()
                .code(item.getCode())
                .name(item.getName())
                .build();
    }

    public static CodeResponseDto fromContentType(ContentType contentType) {
        return CodeResponseDto.builder()
                .code(String.valueOf(contentType.getContentTypeNo()))
                .name(contentType.getContentTypeName())
                .build();
    }
}

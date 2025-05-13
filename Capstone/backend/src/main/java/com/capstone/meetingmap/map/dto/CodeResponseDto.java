package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.map.dto.tourapi.CodeItem;
import com.capstone.meetingmap.map.entity.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//프론트에 각종 코드들을 전달할 dto
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CodeResponseDto {
    private String code;
    private String name;

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

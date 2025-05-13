package com.capstone.meetingmap.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//프론트에 locationBasedList와 각 출발지, 중간 위치를 전달할 dto
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MiddlePointResponseDto<T> {
    private List<String> names;
    private List<XYCoordinate> coordinates;
    private String middleX;
    private String middleY;
    private T list;

    // 기존 place dto에 중간지점 dto를 붙여줌
    public static MiddlePointResponseDto<List<PlaceResponseDto>> addMiddlePointResponse(List<String> names, XYDto xyDto, List<PlaceResponseDto> placeDto) {
        return MiddlePointResponseDto.<List<PlaceResponseDto>>builder()
                .names(names)
                .coordinates(xyDto.getCoordinates())
                .middleX(String.valueOf(xyDto.getMiddleX()))
                .middleY(String.valueOf(xyDto.getMiddleY()))
                .list(placeDto)
                .build();
    }
}

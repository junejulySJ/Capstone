package com.capstone.meetingmap.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//프론트에 locationBasedList와 각 출발지, 중간 위치를 전달할 dto
@Getter
@NoArgsConstructor
public class MiddlePointResponseDto<T> {
    private List<String> addresses;
    private List<XYCoordinate> coordinates;
    private String middleX;
    private String middleY;
    private T list;

    @Builder
    public MiddlePointResponseDto(List<String> addresses, List<XYCoordinate> coordinates, String middleX, String middleY, T list) {
        this.addresses = addresses;
        this.coordinates = coordinates;
        this.middleX = middleX;
        this.middleY = middleY;
        this.list = list;
    }
}

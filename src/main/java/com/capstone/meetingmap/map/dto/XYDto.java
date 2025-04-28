package com.capstone.meetingmap.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class XYDto {
    private List<XYCoordinate> coodinates;
    private Double middleX;
    private Double middleY;

    @Builder
    public XYDto(List<XYCoordinate> coodinates, Double middleX, Double middleY) {
        this.coodinates = coodinates;
        this.middleX = middleX;
        this.middleY = middleY;
    }
}

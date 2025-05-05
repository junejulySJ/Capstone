package com.capstone.meetingmap.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Getter
@NoArgsConstructor
public class XYDto {
    private List<XYCoordinate> coordinates;
    private Double middleX;
    private Double middleY;

    @Builder
    public XYDto(List<XYCoordinate> coordinates, Double middleX, Double middleY) {
        this.coordinates = coordinates;
        this.middleX = middleX;
        this.middleY = middleY;
    }

    public static XYDto buildXYDtoByGeometry(Point middlePoint, List<Coordinate> coordinates) {
        return XYDto.builder()
                .middleX(middlePoint.getX())
                .middleY(middlePoint.getY())
                .coordinates(coordinates.stream()
                        .map(coordinate -> new XYCoordinate(
                                String.valueOf(coordinate.getX()),
                                String.valueOf(coordinate.getY())
                        ))
                        .toList())
                .build();
    }
}

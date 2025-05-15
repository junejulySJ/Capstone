package com.capstone.meetingmap.path.dto;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.api.tmap.dto.RouteResponse;
import com.capstone.meetingmap.map.dto.XYDto;
import com.capstone.meetingmap.schedule.dto.ScheduleDetailCreateDto;
import com.capstone.meetingmap.util.ParseUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathResponseDto {
    private Location origin;
    private Location destination;
    private Integer distance;
    private Integer time;
    private List<List<Double>> coordinates;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        private double x; // longitude
        private double y; // latitude
    }

    public static PathResponseDto fromRouteResponse(RouteResponse routeResponse, ScheduleDetailCreateDto first, ScheduleDetailCreateDto last, List<List<Double>> coordinates) {
        return PathResponseDto.builder()
                .origin(Location.builder()
                        .x(first.getLongitude().doubleValue())
                        .y(first.getLatitude().doubleValue())
                        .build())
                .destination(Location.builder()
                        .x(last.getLongitude().doubleValue())
                        .y(last.getLatitude().doubleValue())
                        .build())
                .distance(routeResponse.getFeatures().get(0).getProperties().getTotalDistance())
                .time(routeResponse.getFeatures().get(0).getProperties().getTotalTime() / 60)
                .coordinates(coordinates)
                .build();
    }

    public static PathResponseDto fromDocuments(RouteResponse routeResponse, AddressFromKeywordResponse.Documents first, AddressFromKeywordResponse.Documents last, List<List<Double>> coordinates) {
        return PathResponseDto.builder()
                .origin(Location.builder()
                        .x(ParseUtil.parseDoubleSafe(first.getX()))
                        .y(ParseUtil.parseDoubleSafe(first.getY()))
                        .build())
                .destination(Location.builder()
                        .x(ParseUtil.parseDoubleSafe(last.getY()))
                        .y(ParseUtil.parseDoubleSafe(last.getY()))
                        .build())
                .distance(routeResponse.getFeatures().get(0).getProperties().getTotalDistance())
                .time(routeResponse.getFeatures().get(0).getProperties().getTotalTime() / 60)
                .coordinates(coordinates)
                .build();
    }

    public static PathResponseDto fromCoordinateAndXyDto(RouteResponse routeResponse, Coordinate first, XYDto last, List<List<Double>> coordinates) {
        return PathResponseDto.builder()
                .origin(PathResponseDto.Location.builder()
                        .x(first.getX())
                        .y(first.getY())
                        .build())
                .destination(PathResponseDto.Location.builder()
                        .x(last.getMiddleX())
                        .y(last.getMiddleY())
                        .build())
                .distance(routeResponse.getFeatures().get(0).getProperties().getTotalDistance())
                .time(routeResponse.getFeatures().get(0).getProperties().getTotalTime() / 60)
                .coordinates(coordinates)
                .build();
    }
}

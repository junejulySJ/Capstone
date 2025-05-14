package com.capstone.meetingmap.path.dto;

import com.capstone.meetingmap.api.tmap.dto.RouteResponse;
import com.capstone.meetingmap.schedule.dto.ScheduleDetailCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}

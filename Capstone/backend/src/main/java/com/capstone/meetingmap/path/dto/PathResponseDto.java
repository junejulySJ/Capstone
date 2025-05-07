package com.capstone.meetingmap.path.dto;

import com.capstone.meetingmap.api.kakaomobility.dto.RouteResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathResponseDto {
    private Location origin;
    private Location destination;
    private Integer distance;
    private List<PathRoad> roads;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        private double x; // longitude
        private double y; // latitude
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PathRoad {
        private List<Double> vertexes; // lng1, lat1, lng2, lat2, ...

        public static List<PathRoad> fromRouteResponseRoad(List<RouteResponse.Road> roads) {
            List<PathRoad> pathRoads = new ArrayList<>();
            for (RouteResponse.Road road : roads) {
                pathRoads.add(PathRoad.builder()
                        .vertexes(road.getVertexes())
                        .build());
            }
            return pathRoads;
        }
    }

    public static PathResponseDto fromRouteResponse(RouteResponse routeResponse) {
        return PathResponseDto.builder()
                .origin(Location.builder()
                        .x(routeResponse.getRoutes().get(0).getSummary().getOrigin().getX())
                        .y(routeResponse.getRoutes().get(0).getSummary().getOrigin().getY())
                        .build())
                .destination(Location.builder()
                        .x(routeResponse.getRoutes().get(0).getSummary().getDestination().getX())
                        .y(routeResponse.getRoutes().get(0).getSummary().getDestination().getY())
                        .build())
                .distance(routeResponse.getRoutes().get(0).getSummary().getDistance())
                .roads(PathRoad.fromRouteResponseRoad(routeResponse.getRoutes().get(0).getSections().get(0).getRoads()))
                .build();
    }
}

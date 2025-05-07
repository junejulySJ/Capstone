package com.capstone.meetingmap.api.kakaomobility.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteResponse {
    private String trans_id;
    private List<Route> routes;

    @Data
    public static class Route {
        private Integer result_code;
        private String result_msg;
        private Summary summary;
        private List<Section> sections;
    }

    @Data
    public static class Summary {
        private Location origin;
        private Location destination;
        private List<Object> waypoints; // 빈 배열로만 존재하므로 Object로 처리
        private String priority;
        private Bound bound;
        private Fare fare;
        private Integer distance;
        private Integer duration;
    }

    @Data
    public static class Location {
        private String name;
        private Double x;
        private Double y;
    }

    @Data
    public static class Bound {
        private Double min_x;
        private Double min_y;
        private Double max_x;
        private Double max_y;
    }

    @Data
    public static class Fare {
        private Integer taxi;
        private Integer toll;
    }

    @Data
    public static class Section {
        private Integer distance;
        private Integer duration;
        private Bound bound;
        private List<Road> roads;
        private List<Guide> guides;
    }

    @Data
    public static class Road {
        private String name;
        private Integer distance;
        private Integer duration;
        private Double traffic_speed;
        private Integer traffic_state;
        private List<Double> vertexes;
    }

    @Data
    public static class Guide {
        private String name;
        private Double x;
        private Double y;
        private Integer distance;
        private Integer duration;
        private Integer type;
        private String guidance;
        private Integer road_index;
    }
}

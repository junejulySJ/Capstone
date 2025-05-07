package com.capstone.meetingmap.api.kakaomobility.dto;

import com.capstone.meetingmap.schedule.dto.SelectedPlace;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KakaoMobilityDirectionsRequest {
    private Origin origin;
    private List<Destination> destinations = new ArrayList<>();
    private int radius;

    @Data
    public static class Origin {
        private String x;
        private String y;
    }

    @Data
    public static class Destination {
        private String x;
        private String y;
        private String key;
    }

    public KakaoMobilityDirectionsRequest(SelectedPlace from, List<SelectedPlace> to) {
        this.origin.x = from.getLongitude();
        this.origin.y = from.getLatitude();
        for (int i = 0; i < to.size(); i++) {
            Destination destination = new Destination();
            destination.x = to.get(i).getLongitude();
            destination.y = to.get(i).getLatitude();
            destination.key = String.valueOf(i);
           this.destinations.add(destination);
        }
    }
}



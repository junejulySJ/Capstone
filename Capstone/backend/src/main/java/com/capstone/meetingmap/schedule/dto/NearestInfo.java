package com.capstone.meetingmap.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NearestInfo {
    SelectedPlace nearest;
    Double minDistance;

    @Builder
    public NearestInfo(SelectedPlace nearest, Double minDistance) {
        this.nearest = nearest;
        this.minDistance = minDistance;
    }
}

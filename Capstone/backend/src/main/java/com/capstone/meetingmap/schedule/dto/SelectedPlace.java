package com.capstone.meetingmap.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SelectedPlace {
    private String contentId;
    private String address;
    private String name;
    private String latitude;
    private String longitude;
    private String category;
    private Integer stayMinutes;
}

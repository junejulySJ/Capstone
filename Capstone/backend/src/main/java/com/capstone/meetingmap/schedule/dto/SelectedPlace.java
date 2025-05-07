package com.capstone.meetingmap.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SelectedPlace {
    private String contentId;
    private String address;
    private String title;
    private String latitude;
    private String longitude;
    private String cat3;
    private Integer stayMinutes;
}

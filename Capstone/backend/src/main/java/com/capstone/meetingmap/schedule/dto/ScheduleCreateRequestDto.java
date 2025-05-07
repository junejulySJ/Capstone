package com.capstone.meetingmap.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequestDto {
    private List<SelectedPlace> selectedPlace;
    private String scheduleName;
    private String scheduleAbout;
    private LocalDateTime scheduleStartTime;
    private LocalDateTime scheduleEndTime;
    private String startContentId;
    private Boolean additionalRecommendation;
    private Integer totalPlaceCount;
    private String theme;
    private Double minimumRating;

    @Builder
    public ScheduleCreateRequestDto(List<SelectedPlace> selectedPlace, String scheduleName, String scheduleAbout, LocalDateTime scheduleStartTime, LocalDateTime scheduleEndTime, String startContentId, Boolean additionalRecommendation, Integer totalPlaceCount, String theme, Double minimumRating) {
        this.selectedPlace = selectedPlace;
        this.scheduleName = scheduleName;
        this.scheduleAbout = scheduleAbout;
        this.scheduleStartTime = scheduleStartTime;
        this.scheduleEndTime = scheduleEndTime;
        this.startContentId = startContentId;
        this.additionalRecommendation = additionalRecommendation;
        this.totalPlaceCount = totalPlaceCount;
        this.theme = theme;
        this.minimumRating = minimumRating;
    }
}

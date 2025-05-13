package com.capstone.meetingmap.schedule.dto;

import com.capstone.meetingmap.schedule.validator.ValidSelectedPlaceCondition;
import com.capstone.meetingmap.schedule.validator.ValidTransportType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ValidSelectedPlaceCondition
public class ScheduleCreateRequestDto {
    @NotEmpty(message = "장소는 반드시 1개 이상 선택해야 합니다")
    @Size(min = 1, message = "장소는 반드시 1개 이상 선택해야 합니다")
    private List<SelectedPlace> selectedPlace;

    @NotBlank(message = "스케줄 이름은 반드시 입력해야 합니다")
    private String scheduleName;

    @NotBlank(message = "스케줄 설명은 반드시 입력해야 합니다")
    private String scheduleAbout;

    @NotNull(message = "스케줄 시작 시간은 반드시 필요합니다")
    @FutureOrPresent(message = "스케줄 시작 시간은 현재 시간 이후만 가능합니다")
    private LocalDateTime scheduleStartTime;

    @NotNull(message = "스케줄 종료 시간은 반드시 필요합니다")
    @FutureOrPresent(message = "스케줄 종료 시간은 현재 시간 이후만 가능합니다")
    private LocalDateTime scheduleEndTime;

    @NotBlank(message = "스케줄 시작 장소는 반드시 입력해야 합니다")
    private String startContentId;

    @NotBlank(message = "이동 수단은 반드시 선택해야 합니다")
    @ValidTransportType
    private String transport;

    @NotNull(message = "추가 추천 여부는 반드시 필요합니다")
    private Boolean additionalRecommendation;

    private Integer totalPlaceCount;
    private String theme;
    private Double minimumRating;
    private Integer stayMinutesMean;
}

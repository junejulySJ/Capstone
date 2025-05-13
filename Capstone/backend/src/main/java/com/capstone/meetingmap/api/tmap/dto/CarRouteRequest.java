package com.capstone.meetingmap.api.tmap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

// 자동차 경로 안내 api 요청을 보낼 dto
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarRouteRequest {
    private Double startX;
    private Double startY;
    private Double endX;
    private Double endY;
    private String passList;
}

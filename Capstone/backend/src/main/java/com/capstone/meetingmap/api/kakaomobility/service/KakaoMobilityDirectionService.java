package com.capstone.meetingmap.api.kakaomobility.service;

import com.capstone.meetingmap.api.kakaomobility.dto.RouteResponse;
import com.capstone.meetingmap.path.dto.PathResponseDto;
import com.capstone.meetingmap.schedule.dto.ScheduleDetailCreateDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KakaoMobilityDirectionService {
    private final RestClient kakaoMobilityRestClient;

    public KakaoMobilityDirectionService(RestClient kakaoMobilityRestClient) {
        this.kakaoMobilityRestClient = kakaoMobilityRestClient;
    }

    public List<PathResponseDto> getPath(List<ScheduleDetailCreateDto> dtoList) {
        List<RouteResponse> routeResponseList = new ArrayList<>();
        for (int i = 0; i < dtoList.size() - 1; i++) {
            final int index = i;
            RouteResponse routeResponse = kakaoMobilityRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/directions")
                            .queryParam("origin", dtoList.get(index).getLongitude() + "," + dtoList.get(index).getLatitude())
                            .queryParam("destination", dtoList.get(index + 1).getLongitude() + "," + dtoList.get(index + 1).getLatitude())
                            .build())
                    .retrieve()
                    .body(RouteResponse.class);
            routeResponseList.add(routeResponse);
        }
        return routeResponseList.stream()
                .map(PathResponseDto::fromRouteResponse)
        .collect(Collectors.toList());
    }
}

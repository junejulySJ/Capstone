package com.capstone.meetingmap.path.service;

import com.capstone.meetingmap.api.tmap.dto.PedestrianRouteRequest;
import com.capstone.meetingmap.api.tmap.dto.RouteRequest;
import com.capstone.meetingmap.api.tmap.dto.RouteResponse;
import com.capstone.meetingmap.api.tmap.dto.TransitRouteResponse;
import com.capstone.meetingmap.api.tmap.service.TMapApiService;
import com.capstone.meetingmap.path.dto.PathResponseDto;
import com.capstone.meetingmap.path.dto.TransitPathResponseDto;
import com.capstone.meetingmap.schedule.dto.ScheduleDetailCreateDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final TMapApiService tMapApiService;

    public PathService(TMapApiService tMapApiService) {
        this.tMapApiService = tMapApiService;
    }

    // 보행자 경로 출력
    public List<PathResponseDto> getPedestrianPath(List<ScheduleDetailCreateDto> dtoList) {
        if (dtoList.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경로를 그리기 위한 장소가 너무 적습니다");
        }

        List<PathResponseDto> pathResponseDtoList = new ArrayList<>();
        for (int i = 0; i < dtoList.size() - 1; i++) {
            RouteResponse routeResponse = tMapApiService.getPedestrianRoutes(PedestrianRouteRequest.builder()
                    .startX(dtoList.get(i).getLongitude().doubleValue())
                    .startY(dtoList.get(i).getLatitude().doubleValue())
                    .endX(dtoList.get(i + 1).getLongitude().doubleValue())
                    .endY(dtoList.get(i + 1).getLatitude().doubleValue())
                    .build());

            pathResponseDtoList.add(PathResponseDto.fromRouteResponse(routeResponse, dtoList.get(i), dtoList.get(i + 1), getCoordinates(routeResponse)));
        }
        return pathResponseDtoList;
    }

    // 자동차 경로 출력
    public List<PathResponseDto> getCarPath(List<ScheduleDetailCreateDto> dtoList) {
        if (dtoList.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경로를 그리기 위한 장소가 너무 적습니다");
        }

        List<PathResponseDto> pathResponseDtoList = new ArrayList<>();
        for (int i = 0; i < dtoList.size() - 1; i++) {
            RouteResponse routeResponse = tMapApiService.getCarRoutes(RouteRequest.builder()
                    .startX(dtoList.get(i).getLongitude().doubleValue())
                    .startY(dtoList.get(i).getLatitude().doubleValue())
                    .endX(dtoList.get(i + 1).getLongitude().doubleValue())
                    .endY(dtoList.get(i + 1).getLatitude().doubleValue())
                    .build());

            pathResponseDtoList.add(PathResponseDto.fromRouteResponse(routeResponse, dtoList.get(i), dtoList.get(i + 1), getCoordinates(routeResponse)));
        }
        return pathResponseDtoList;
    }

    // 대중교통 경로 출력
    public List<TransitPathResponseDto> getTransitPath(List<ScheduleDetailCreateDto> dtoList) {
        if (dtoList.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경로를 그리기 위한 장소가 너무 적습니다");
        }

        List<TransitPathResponseDto> pathResponseDtoList = new ArrayList<>();
        for (int i = 0; i < dtoList.size() - 1; i++) {
            TransitRouteResponse transitRouteResponse = tMapApiService.getTransitRoutes(RouteRequest.builder()
                    .startX(dtoList.get(i).getLongitude().doubleValue())
                    .startY(dtoList.get(i).getLatitude().doubleValue())
                    .endX(dtoList.get(i + 1).getLongitude().doubleValue())
                    .endY(dtoList.get(i + 1).getLatitude().doubleValue())
                    .build());

            pathResponseDtoList.add(TransitPathResponseDto.fromTransitRouteResponse(transitRouteResponse, dtoList.get(i), dtoList.get(i + 1), TransitPathResponseDto.getPlan(transitRouteResponse)));
        }
        return pathResponseDtoList;
    }

    // 경로 좌표 변환
    public static List<List<Double>> getCoordinates(RouteResponse routeResponse) {
        return routeResponse.getFeatures().stream()
                .filter(feature -> "LineString".equals(feature.getGeometry().getType()))
                .flatMap(feature -> {
                    Object coordsObj = feature.getGeometry().getCoordinates();
                    List<List<Object>> coords = (List<List<Object>>) coordsObj;

                    return coords.stream()
                            .map(coord -> coord.stream()
                                    .map(o -> ((Number) o).doubleValue())
                                    .collect(Collectors.toList()));
                })
                .toList();
    }


}

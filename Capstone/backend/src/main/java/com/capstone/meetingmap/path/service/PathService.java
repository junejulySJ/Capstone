package com.capstone.meetingmap.path.service;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.api.kakao.service.KakaoApiService;
import com.capstone.meetingmap.api.tmap.dto.PedestrianRouteRequest;
import com.capstone.meetingmap.api.tmap.dto.RouteRequest;
import com.capstone.meetingmap.api.tmap.dto.RouteResponse;
import com.capstone.meetingmap.api.tmap.dto.TransitRouteResponse;
import com.capstone.meetingmap.api.tmap.service.TMapApiService;
import com.capstone.meetingmap.map.dto.XYDto;
import com.capstone.meetingmap.map.service.ConvexHullService;
import com.capstone.meetingmap.path.dto.PathResponseDto;
import com.capstone.meetingmap.path.dto.TransitPathResponseDto;
import com.capstone.meetingmap.schedule.dto.ScheduleDetailCreateDto;
import com.capstone.meetingmap.util.ClampUtil;
import com.capstone.meetingmap.util.ParseUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final TMapApiService tMapApiService;
    private final KakaoApiService kakaoApiService;
    private final ConvexHullService convexHullService;

    public PathService(TMapApiService tMapApiService, KakaoApiService kakaoApiService, ConvexHullService convexHullService) {
        this.tMapApiService = tMapApiService;
        this.kakaoApiService = kakaoApiService;
        this.convexHullService = convexHullService;
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

    // 장소 이름으로 도보 경로 출력
    public List<PathResponseDto> getPedestrianPathByName(List<String> names) {
        if (names.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경로를 그리기 위한 장소가 너무 적습니다");
        } else if (names.size() == 2) {
            AddressFromKeywordResponse start = kakaoApiService.getAddressFromKeyword(names.get(0));
            AddressFromKeywordResponse end = kakaoApiService.getAddressFromKeyword(names.get(1));

            List<PathResponseDto> pathResponseDtoList = new ArrayList<>();
            RouteResponse routeResponse = tMapApiService.getPedestrianRoutes(PedestrianRouteRequest.builder()
                    .startX(ParseUtil.parseDoubleSafe(start.getDocuments().get(0).getX()))
                    .startY(ParseUtil.parseDoubleSafe(start.getDocuments().get(0).getY()))
                    .endX(ParseUtil.parseDoubleSafe(end.getDocuments().get(0).getX()))
                    .endY(ParseUtil.parseDoubleSafe(end.getDocuments().get(0).getY()))
                    .build());
            pathResponseDtoList.add(PathResponseDto.fromDocuments(routeResponse, start.getDocuments().get(0), end.getDocuments().get(0), getCoordinates(routeResponse)));
            return pathResponseDtoList;
        } else {
            List<Coordinate> coordList = kakaoApiService.getCoordList(names);
            Point middlePoint = convexHullService.calculateConvexHullCentroid(coordList);
            Point adjustedMiddlePoint = ClampUtil.clampPoint(middlePoint);

            XYDto xyDto = XYDto.buildXYDtoByGeometry(adjustedMiddlePoint, coordList);

            List<PathResponseDto> pathResponseDtoList = new ArrayList<>();
            for (Coordinate coordinate : coordList) {
                RouteResponse routeResponse = tMapApiService.getPedestrianRoutes(PedestrianRouteRequest.builder()
                        .startX(ParseUtil.parseDoubleSafe(String.valueOf(coordinate.getX())))
                        .startY(ParseUtil.parseDoubleSafe(String.valueOf(coordinate.getY())))
                        .endX(ParseUtil.parseDoubleSafe(String.valueOf(xyDto.getMiddleX())))
                        .endY(ParseUtil.parseDoubleSafe(String.valueOf(xyDto.getMiddleY())))
                        .build());

                pathResponseDtoList.add(PathResponseDto.fromCoordinateAndXyDto(routeResponse, coordinate, xyDto, getCoordinates(routeResponse)));
            }
            return pathResponseDtoList;
        }
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

    // 장소 이름으로 자동차 경로 출력
    public List<PathResponseDto> getCarPathByName(List<String> names) {
        if (names.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경로를 그리기 위한 장소가 너무 적습니다");
        } else if (names.size() == 2) {
            AddressFromKeywordResponse start = kakaoApiService.getAddressFromKeyword(names.get(0));
            AddressFromKeywordResponse end = kakaoApiService.getAddressFromKeyword(names.get(1));

            List<PathResponseDto> pathResponseDtoList = new ArrayList<>();
            RouteResponse routeResponse = tMapApiService.getCarRoutes(RouteRequest.builder()
                    .startX(ParseUtil.parseDoubleSafe(start.getDocuments().get(0).getX()))
                    .startY(ParseUtil.parseDoubleSafe(start.getDocuments().get(0).getY()))
                    .endX(ParseUtil.parseDoubleSafe(end.getDocuments().get(0).getX()))
                    .endY(ParseUtil.parseDoubleSafe(end.getDocuments().get(0).getY()))
                    .build());
            pathResponseDtoList.add(PathResponseDto.fromDocuments(routeResponse, start.getDocuments().get(0), end.getDocuments().get(0), getCoordinates(routeResponse)));
            return pathResponseDtoList;
        } else {
            List<Coordinate> coordList = kakaoApiService.getCoordList(names);
            Point middlePoint = convexHullService.calculateConvexHullCentroid(coordList);
            Point adjustedMiddlePoint = ClampUtil.clampPoint(middlePoint);

            XYDto xyDto = XYDto.buildXYDtoByGeometry(adjustedMiddlePoint, coordList);

            List<PathResponseDto> pathResponseDtoList = new ArrayList<>();
            for (Coordinate coordinate : coordList) {
                RouteResponse routeResponse = tMapApiService.getCarRoutes(RouteRequest.builder()
                        .startX(ParseUtil.parseDoubleSafe(String.valueOf(coordinate.getX())))
                        .startY(ParseUtil.parseDoubleSafe(String.valueOf(coordinate.getY())))
                        .endX(ParseUtil.parseDoubleSafe(String.valueOf(xyDto.getMiddleX())))
                        .endY(ParseUtil.parseDoubleSafe(String.valueOf(xyDto.getMiddleY())))
                        .build());

                pathResponseDtoList.add(PathResponseDto.fromCoordinateAndXyDto(routeResponse, coordinate, xyDto, getCoordinates(routeResponse)));
            }
            return pathResponseDtoList;
        }
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

            pathResponseDtoList.add(TransitPathResponseDto.fromScheduleDetailCreateDto(dtoList.get(i), dtoList.get(i + 1), TransitPathResponseDto.getPlan(transitRouteResponse)));
        }
        return pathResponseDtoList;
    }

    // 장소 이름으로 대중교통 경로 출력
    public List<TransitPathResponseDto> getTransitPathByName(List<String> names) {
        if (names.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경로를 그리기 위한 장소가 너무 적습니다");
        } else if (names.size() == 2) {
            AddressFromKeywordResponse start = kakaoApiService.getAddressFromKeyword(names.get(0));
            AddressFromKeywordResponse end = kakaoApiService.getAddressFromKeyword(names.get(1));

            List<TransitPathResponseDto> pathResponseDtoList = new ArrayList<>();
            TransitRouteResponse transitRouteResponse = tMapApiService.getTransitRoutes(RouteRequest.builder()
                    .startX(ParseUtil.parseDoubleSafe(start.getDocuments().get(0).getX()))
                    .startY(ParseUtil.parseDoubleSafe(start.getDocuments().get(0).getY()))
                    .endX(ParseUtil.parseDoubleSafe(end.getDocuments().get(0).getX()))
                    .endY(ParseUtil.parseDoubleSafe(end.getDocuments().get(0).getY()))
                    .build());

            pathResponseDtoList.add(TransitPathResponseDto.fromDocuments(start.getDocuments().get(0), end.getDocuments().get(0), TransitPathResponseDto.getPlan(transitRouteResponse)));
            return pathResponseDtoList;
        } else {
            List<Coordinate> coordList = kakaoApiService.getCoordList(names);
            Point middlePoint = convexHullService.calculateConvexHullCentroid(coordList);
            Point adjustedMiddlePoint = ClampUtil.clampPoint(middlePoint);

            XYDto xyDto = XYDto.buildXYDtoByGeometry(adjustedMiddlePoint, coordList);

            List<TransitPathResponseDto> pathResponseDtoList = new ArrayList<>();
            for (Coordinate coordinate : coordList) {
                TransitRouteResponse transitRouteResponse = tMapApiService.getTransitRoutes(RouteRequest.builder()
                        .startX(ParseUtil.parseDoubleSafe(String.valueOf(coordinate.getX())))
                        .startY(ParseUtil.parseDoubleSafe(String.valueOf(coordinate.getY())))
                        .endX(ParseUtil.parseDoubleSafe(String.valueOf(xyDto.getMiddleX())))
                        .endY(ParseUtil.parseDoubleSafe(String.valueOf(xyDto.getMiddleY())))
                        .build());

                pathResponseDtoList.add(TransitPathResponseDto.fromCoordinateAndXyDto(coordinate, xyDto, TransitPathResponseDto.getPlan(transitRouteResponse)));
            }
            return pathResponseDtoList;
        }
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

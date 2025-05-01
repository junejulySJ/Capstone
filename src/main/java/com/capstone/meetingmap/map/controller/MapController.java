package com.capstone.meetingmap.map.controller;

import com.capstone.meetingmap.map.dto.*;
import com.capstone.meetingmap.map.service.ConvexHullService;
import com.capstone.meetingmap.map.service.KakaoMapService;
import com.capstone.meetingmap.map.service.MapService;
import com.capstone.meetingmap.map.service.TourApiMapService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final TourApiMapService tourApiMapService;
    private final KakaoMapService kakaoMapService;
    private final ConvexHullService convexHullService;
    private final MapService mapService;

    public MapController(TourApiMapService tourApiMapService, KakaoMapService kakaoMapService, ConvexHullService convexHullService, MapService mapService) {
        this.tourApiMapService = tourApiMapService;
        this.kakaoMapService = kakaoMapService;
        this.convexHullService = convexHullService;
        this.mapService = mapService;
    }

    //지역/시군구 코드 반환
    @GetMapping("/region")
    public ResponseEntity<List<CodeResponseDto>> getRegionCodes(@RequestParam(value = "code", required = false) String areaCode) {
        List<CodeResponseDto> codeResponseDtoList = tourApiMapService.getRegionCodes(areaCode);
        return ResponseEntity.ok(codeResponseDtoList);
    }

    //지도 출력
    @GetMapping
    public ResponseEntity<?> getMap(
            @RequestParam(value = "search") String search, //지도 출력 방법
            @RequestParam(value = "areaCode", required = false) String areaCode,
            @RequestParam(value = "sigunguCode", required = false) String sigunguCode,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "address", required = false) List<String> addresses,
            @RequestParam(value = "theme") String theme
    ) {

        switch (search) {
            case "area" -> { //"area"면 areaCode, sigunguCode, theme 필요
                return ResponseEntity.ok(mapService.getPlacesByArea(areaCode, sigunguCode, theme));
            }
            case "location" -> { //"location"이면 latitude, longitude, theme 필요
                List<String> area = kakaoMapService.getAreaFromCoordinate(String.valueOf(longitude), String.valueOf(latitude));
                String searchedAreaCode = tourApiMapService.findAreaCodeByName(area.get(0));
                String searchedSigunguCode = tourApiMapService.findSigunguCodeByName(area.get(1), searchedAreaCode);

                return ResponseEntity.ok(mapService.getPlacesByArea(searchedAreaCode, searchedSigunguCode, theme));
            }
            case "middle-point", "middle-point2" -> { //"middle-point", "middle-point2"면 address, theme 필요
                XYDto xyDto;
                if (search.equals("middle-point")) {
                    xyDto = kakaoMapService.getMiddlePoint(addresses);
                } else { // middle-point2
                    List<Coordinate> coordList = kakaoMapService.getCoordList(addresses);
                    Point middlePoint = convexHullService.calculateConvexHullCentroid(coordList);
                    xyDto = XYDto.buildXYDtoByGeometry(middlePoint, coordList);
                }
                List<String> area = kakaoMapService.getAreaFromCoordinate(String.valueOf(xyDto.getMiddleX()), String.valueOf(xyDto.getMiddleY()));

                String searchedAreaCode = tourApiMapService.findAreaCodeByName(area.get(0));
                String searchedSigunguCode = tourApiMapService.findSigunguCodeByName(area.get(1), searchedAreaCode);

                List<PlaceResponseDto> places = mapService.getPlacesByArea(searchedAreaCode, searchedSigunguCode, theme);
                return ResponseEntity.ok(tourApiMapService.getPlacesByMiddlePoint(addresses, xyDto, places));
            }
            default -> throw new IllegalStateException("올바르지 않은 검색 방법입니다");
        }
    }

    //세부 정보 출력
    @GetMapping("/detail")
    public ResponseEntity<DetailCommonResponseDto> getPlaceDetail(@RequestParam(value = "contentId") String contentId) {
        try {
            DetailCommonResponseDto detailCommonResponseDto = tourApiMapService.getPlaceDetail(contentId);
            return ResponseEntity.ok(detailCommonResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

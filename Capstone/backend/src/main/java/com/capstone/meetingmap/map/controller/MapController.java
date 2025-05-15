package com.capstone.meetingmap.map.controller;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.api.kakao.service.KakaoApiService;
import com.capstone.meetingmap.map.dto.*;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoCoordinateSearchResponse;
import com.capstone.meetingmap.map.service.ConvexHullService;
import com.capstone.meetingmap.map.service.MapService;
import com.capstone.meetingmap.map.service.TourApiMapService;
import com.capstone.meetingmap.util.ClampUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final TourApiMapService tourApiMapService;
    private final ConvexHullService convexHullService;
    private final MapService mapService;
    private final KakaoApiService kakaoApiService;

    public MapController(TourApiMapService tourApiMapService, ConvexHullService convexHullService, MapService mapService, KakaoApiService kakaoApiService) {
        this.tourApiMapService = tourApiMapService;
        this.convexHullService = convexHullService;
        this.mapService = mapService;
        this.kakaoApiService = kakaoApiService;
    }

    // 카테고리 코드 반환
    @GetMapping("/category")
    public ResponseEntity<?> getCategoryCodes(@RequestParam(value = "category", required = false) String category) {
        List<CodeResponseDto> codeResponseDtoList = mapService.getCategoryCodes(category);
        return ResponseEntity.ok(codeResponseDtoList);
    }

    // 지도 출력
    @GetMapping
    public ResponseEntity<?> getMap(
            @RequestParam(value = "search") String search, //장소 선택 방법
            @RequestParam(value = "sort") String sort, // 정렬 방법
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "name", required = false) List<String> name,
            @RequestParam(value = "category", required = false) String category
    ) {

        switch (search) {
            case "destination" -> { // 출발지-도착지 기반 검색이면 start, end 필요
                AddressFromKeywordResponse startResponse = kakaoApiService.getAddressFromKeyword(start);
                AddressFromKeywordResponse endResponse = kakaoApiService.getAddressFromKeyword(end);
                List<PlaceResponseDto> places = mapService.getAllPlaces(sort, endResponse.getDocuments().get(0).getY(), endResponse.getDocuments().get(0).getX(), category);
                return ResponseEntity.ok(PointResponseDto.addDestinationResponse(startResponse, endResponse, places));
            }
            case "location" -> { // 현재 위치 기반 검색이면 latitude, longitude 필요
                return ResponseEntity.ok(mapService.getAllPlaces(sort, String.valueOf(latitude), String.valueOf(longitude), category));
            }
            case "middle-point" -> { // 중간 위치 기반 검색이면 name 리스트 필요
                List<AddressFromKeywordResponse> startResponseList = new ArrayList<>();
                for (String placeName : name) {
                    AddressFromKeywordResponse response = kakaoApiService.getAddressFromKeyword(placeName);
                    startResponseList.add(response);
                }
                List<Coordinate> coordList = kakaoApiService.getCoordList(name);
                Point middlePoint = convexHullService.calculateConvexHullCentroid(coordList);
                Point adjustedMiddlePoint = ClampUtil.clampPoint(middlePoint);

                XYDto xyDto = XYDto.buildXYDtoByGeometry(adjustedMiddlePoint, coordList);

                KakaoCoordinateSearchResponse response = kakaoApiService.getAddressFromCoordinate(xyDto.getMiddleX(), xyDto.getMiddleY());

                List<PlaceResponseDto> places = mapService.getAllPlaces(sort, String.valueOf(xyDto.getMiddleY()), String.valueOf(xyDto.getMiddleX()), category);

                return ResponseEntity.ok(MiddlePointResponseDto.addMiddlePointResponse(startResponseList, response, xyDto, places));
            }
            default -> throw new IllegalStateException("올바르지 않은 검색 방법입니다");
        }
    }

    // 세부 정보 출력
    @GetMapping("/detail")
    public ResponseEntity<DetailCommonResponseDto> getPlaceDetail(@RequestParam(value = "contentId") String contentId) {
        DetailCommonResponseDto detailCommonResponseDto = tourApiMapService.getPlaceDetail(contentId);
        return ResponseEntity.ok(detailCommonResponseDto);
    }

    // 장소명 자동완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> getAddressAutoComplete(@RequestParam(value = "name") String name) {
        List<AddressAutocompleteDto> addressAutocompleteDtoList = mapService.getAddressAutocomplete(name);
        return ResponseEntity.ok(addressAutocompleteDtoList);
    }
}

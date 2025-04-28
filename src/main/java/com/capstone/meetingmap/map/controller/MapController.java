package com.capstone.meetingmap.map.controller;

import com.capstone.meetingmap.map.dto.*;
import com.capstone.meetingmap.map.service.KakaoMapService;
import com.capstone.meetingmap.map.service.TourApiMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final TourApiMapService tourApiMapService;
    private final KakaoMapService kakaoMapService;

    public MapController(TourApiMapService tourApiMapService, KakaoMapService kakaoMapService) {
        this.tourApiMapService = tourApiMapService;
        this.kakaoMapService = kakaoMapService;
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
            @RequestParam(value = "contentTypeId") String contentTypeId
    ) {
        switch (search) {
            case "area" -> { //"area"면 areaCode, sigunguCode, contentTypeId 필요
                List<AreaBasedListResponseDto> areaBasedListResponseDtoList = tourApiMapService.getAreaBasedMap(areaCode, sigunguCode, contentTypeId);
                return ResponseEntity.ok(areaBasedListResponseDtoList);
            }
            case "location" -> { //"location"이면 latitude, longitude, contentTypeId 필요
                List<LocationBasedListResponseDto> locationBasedListResponseDtoList = tourApiMapService.getLocationBasedMap(String.valueOf(longitude), String.valueOf(latitude), "1000", contentTypeId);
                return ResponseEntity.ok(locationBasedListResponseDtoList);
            }
            case "middle-point" -> { //"middle-point"면 address, contentTypeId 필요
                XYDto xyDto = kakaoMapService.getMiddlePoint(addresses);
                MiddlePointResponseDto<List<LocationBasedListResponseDto>> responseDto = tourApiMapService.getMiddlePointBasedMap(addresses, xyDto.getCoodinates(), String.valueOf(xyDto.getMiddleX()), String.valueOf(xyDto.getMiddleY()), "1000", contentTypeId);
                return ResponseEntity.ok(responseDto);
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

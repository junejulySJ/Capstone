package com.capstone.meetingmap.map.controller;

import com.capstone.meetingmap.map.dto.CodeResponseDto;
import com.capstone.meetingmap.map.dto.DetailCommonResponseDto;
import com.capstone.meetingmap.map.dto.PlaceResponseDto;
import com.capstone.meetingmap.map.dto.XYDto;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoAddressSearchResponse;
import com.capstone.meetingmap.map.service.ConvexHullService;
import com.capstone.meetingmap.map.service.KakaoMapService;
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

    //시군구 코드 반환
    @GetMapping("/region")
    public ResponseEntity<List<CodeResponseDto>> getRegionCodes() {
        List<CodeResponseDto> codeResponseDtoList = tourApiMapService.getRegionCodes();
        return ResponseEntity.ok(codeResponseDtoList);
    }

    //타입 코드 반환
    @GetMapping("/type")
    public ResponseEntity<List<CodeResponseDto>> getTypeCodes() {
        List<CodeResponseDto> codeResponseDtoList = mapService.getTypeCodes();
        return ResponseEntity.ok(codeResponseDtoList);
    }

    //분류 코드 반환
    @GetMapping("/category")
    public ResponseEntity<List<CodeResponseDto>> getCategoryCodes(
            @RequestParam(value = "typeCode") String typeCode,
            @RequestParam(value = "cat1", required = false) String cat1,
            @RequestParam(value = "cat2", required = false) String cat2
    ) {
        List<CodeResponseDto> codeResponseDtoList = tourApiMapService.getCategoryCodes(typeCode, cat1, cat2);
        return ResponseEntity.ok(codeResponseDtoList);
    }

    //지도 출력
    @GetMapping
    public ResponseEntity<?> getMap(
            @RequestParam(value = "search") String search, //장소 선택 방법
            @RequestParam(value = "sort") String sort, // 정렬 방법
            @RequestParam(value = "sigunguCode", required = false) String sigunguCode,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "address", required = false) List<String> addresses,
            @RequestParam(value = "typeCode", required = false) String typeCode,
            @RequestParam(value = "cat1", required = false) String cat1,
            @RequestParam(value = "cat2", required = false) String cat2,
            @RequestParam(value = "cat3", required = false) String cat3
    ) {

        switch (search) {
            case "area" -> { //"area"면 sigunguCode 필요
                return ResponseEntity.ok(mapService.getAllPlaces(sort, sigunguCode, null, null, typeCode, cat1, cat2, cat3));
            }
            case "address" -> {//"address"면 address 필요
                KakaoAddressSearchResponse response = kakaoMapService.getPoint(addresses.get(0));
                return ResponseEntity.ok(mapService.getAllPlaces(sort, null, response.getDocuments().get(0).getY(), response.getDocuments().get(0).getX(), typeCode, cat1, cat2, cat3));
            }
            case "location" -> { //"location"이면 latitude, longitude 필요
                return ResponseEntity.ok(mapService.getAllPlaces(sort, null, String.valueOf(latitude), String.valueOf(longitude), typeCode, cat1, cat2, cat3));
            }
            case "middle-point", "middle-point2" -> { //"middle-point", "middle-point2"면 address 리스트
                XYDto xyDto;
                if (search.equals("middle-point")) {
                    xyDto = kakaoMapService.getMiddlePoint(addresses);
                } else { // middle-point2
                    List<Coordinate> coordList = kakaoMapService.getCoordList(addresses);
                    Point middlePoint = convexHullService.calculateConvexHullCentroid(coordList);
                    Point adjustedMiddlePoint = ClampUtil.clampPoint(middlePoint);

                    xyDto = XYDto.buildXYDtoByGeometry(adjustedMiddlePoint, coordList);
                }

                List<PlaceResponseDto> places = mapService.getAllPlaces(sort, null, String.valueOf(xyDto.getMiddleY()), String.valueOf(xyDto.getMiddleX()), typeCode, cat1, cat2, cat3);
                return ResponseEntity.ok(mapService.getPlacesByMiddlePoint(addresses, xyDto, places));
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

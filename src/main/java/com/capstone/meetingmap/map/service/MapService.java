package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.map.dto.MiddlePointResponseDto;
import com.capstone.meetingmap.map.dto.PlaceResponseDto;
import com.capstone.meetingmap.map.dto.XYDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {

    private final TourApiMapService tourApiMapService;

    public MapService(TourApiMapService tourApiMapService) {
        this.tourApiMapService = tourApiMapService;
    }

    // theme별로 분류해 장소 조회 후 리스트 merge
    public List<PlaceResponseDto> getAllPlaces(String areaCode, String sigunguCode, String address, String latitude, String longitude, String theme) {
        List<PlaceResponseDto> mergedList = new ArrayList<>();
        switch (theme) {
            // 관광 명소->관광지(12), 음식점(39)
            case "tour" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "12", 20, null, null, null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude,"39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 자연 힐링->관광지(12)/자연(A01), 음식점(39)
            case "nature" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "12", 20, "A01", null, null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 역사 탐방->관광지(12)/인문(A02)/역사관광지(A0201), 음식점(39)
            case "history" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "12", 20, "A02", "A0201", null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 맛집 투어->음식점(39)
            case "food" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "39", 25, null, null, null);
                mergedList.addAll(list1);
            }
            // 쇼핑->쇼핑(38), 음식점(39)
            case "shopping" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "38", 20, null, null, null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 액티비티->레포츠(28), 음식점(39)
            case "activity" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "28", 20, null, null, null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 데이트->분류(카페), 키워드(전망대, 공원, 한옥마을, 궁)
            case "date" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, null, 10, "A05", "A0502", "A05020900");
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "12", 1, "A02", "A0205", "A02050600");
                mergedList.addAll(list2);
                List<PlaceResponseDto> list3 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, null, 10, "A02", "A0202", "A02020700");
                mergedList.addAll(list3);
                List<PlaceResponseDto> list4 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "12", 1, "A02", "A0201", "A02010600");
                mergedList.addAll(list4);
                List<PlaceResponseDto> list5 = tourApiMapService.getPlaceList(areaCode, sigunguCode, address, longitude, latitude, "12", 3, "A02", "A0201", "A02010100");
                mergedList.addAll(list5);
            }
        }
        return mergedList;
    }

    // 기존 place dto에 중간지점 dto를 붙여줌, 위치 이동 예정
    public MiddlePointResponseDto<List<PlaceResponseDto>> getPlacesByMiddlePoint(List<String> addresses, XYDto xyDto, List<PlaceResponseDto> placeDto) {
        return MiddlePointResponseDto.<List<PlaceResponseDto>>builder()
                .addresses(addresses)
                .coordinates(xyDto.getCoordinates())
                .middleX(String.valueOf(xyDto.getMiddleX()))
                .middleY(String.valueOf(xyDto.getMiddleY()))
                .list(placeDto)
                .build();
    }
}

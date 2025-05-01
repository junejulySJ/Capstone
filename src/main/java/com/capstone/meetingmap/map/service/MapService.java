package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.map.dto.PlaceResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {

    private final TourApiMapService tourApiMapService;

    public MapService(TourApiMapService tourApiMapService) {
        this.tourApiMapService = tourApiMapService;
    }

    public List<PlaceResponseDto> getPlacesByArea(String areaCode, String sigunguCode, String theme) {
        List<PlaceResponseDto> mergedList = new ArrayList<>();
        switch (theme) {
            // 관광 명소->관광지(12), 음식점(39)
            case "tour" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "12", 20, null, null, null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 자연 힐링->관광지(12)/자연(A01), 음식점(39)
            case "nature" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "12", 20, "A01", null, null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 역사 탐방->관광지(12)/인문(A02)/역사관광지(A0201), 음식점(39)
            case "history" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "12", 20, "A02", "A0201", null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 맛집 투어->음식점(39)
            case "food" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "39", 25, null, null, null);
                mergedList.addAll(list1);
            }
            // 쇼핑->쇼핑(38), 음식점(39)
            case "shopping" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "38", 20, null, null, null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 액티비티->레포츠(28), 음식점(39)
            case "activity" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "28", 20, null, null, null);
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, "39", 5, null, null, null);
                mergedList.addAll(list2);
            }
            // 데이트->분류(카페), 키워드(전망대, 공원, 한옥마을, 궁)
            case "date" -> {
                List<PlaceResponseDto> list1 = tourApiMapService.getAreaBasedList(areaCode, sigunguCode, null, 10, "A05", "A0502", "A05020900");
                mergedList.addAll(list1);
                List<PlaceResponseDto> list2 = tourApiMapService.getKeywordBasedList(areaCode, sigunguCode, null, "전망대", 1, null, null, null);
                mergedList.addAll(list2);
                List<PlaceResponseDto> list3 = tourApiMapService.getKeywordBasedList(areaCode, sigunguCode, null, "공원", 10, null, null, null);
                mergedList.addAll(list3);
                List<PlaceResponseDto> list4 = tourApiMapService.getKeywordBasedList(areaCode, sigunguCode, null, "한옥마을", 1, null, null, null);
                mergedList.addAll(list4);
                List<PlaceResponseDto> list5 = tourApiMapService.getKeywordBasedList(areaCode, sigunguCode, "12", "궁", 3, null, null, null);
                mergedList.addAll(list5);
            }
        }
        return mergedList;
    }
}

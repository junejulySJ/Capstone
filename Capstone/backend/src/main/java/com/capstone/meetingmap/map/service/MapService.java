package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.api.kakao.service.KakaoApiService;
import com.capstone.meetingmap.map.dto.AddressAutocompleteDto;
import com.capstone.meetingmap.map.dto.CodeResponseDto;
import com.capstone.meetingmap.map.dto.PlaceResponseDto;
import com.capstone.meetingmap.map.dto.TourApiPlaceResponse;
import com.capstone.meetingmap.map.entity.PlaceCategory;
import com.capstone.meetingmap.map.entity.PlaceCategoryDetail;
import com.capstone.meetingmap.map.repository.PlaceCategoryDetailRepository;
import com.capstone.meetingmap.map.repository.PlaceCategoryRepository;
import com.capstone.meetingmap.util.ParseUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapService {

    private final TourApiMapService tourApiMapService;
    private final GoogleMapService googleMapService;
    private final KakaoApiService kakaoApiService;
    private final PlaceCategoryDetailRepository placeCategoryDetailRepository;
    private final PlaceCategoryRepository placeCategoryRepository;

    public MapService(TourApiMapService tourApiMapService, GoogleMapService googleMapService, KakaoApiService kakaoApiService, PlaceCategoryDetailRepository placeCategoryDetailRepository, PlaceCategoryRepository placeCategoryRepository) {
        this.tourApiMapService = tourApiMapService;
        this.googleMapService = googleMapService;
        this.kakaoApiService = kakaoApiService;
        this.placeCategoryDetailRepository = placeCategoryDetailRepository;
        this.placeCategoryRepository = placeCategoryRepository;
    }

    // 카테고리를 반환
    public List<CodeResponseDto> getCategoryCodes(String category) {
        if (category == null || category.isEmpty()) {
            List<PlaceCategory> placeCategoryList = placeCategoryRepository.findByPlaceCategoryCodeNot("other");
            return placeCategoryList.stream()
                    .map(CodeResponseDto::fromPlaceCategory)
                    .collect(Collectors.toList());
        } else {
            List<PlaceCategoryDetail> placeCategoryDetailList = placeCategoryDetailRepository.findByPlaceCategoryDetailCodeStartingWith(category + "-");
            return placeCategoryDetailList.stream()
                    .map(CodeResponseDto::fromPlaceCategoryDetail)
                    .collect(Collectors.toList());
        }
    }

    // TourAPI 결과와 Google Places API 결과를 합쳐서 출력
    public List<PlaceResponseDto> getAllPlaces(String sort, String latitude, String longitude, String category) {
        List<PlaceCategoryDetail> placeCategoryDetailList = new ArrayList<>();
        List<PlaceResponseDto> mergedList = new ArrayList<>();
        int count;
        // 카테고리를 선택한 경우
        if (category != null) {
            // 매핑되는 코드 가져오기
            PlaceCategoryDetail detail = placeCategoryDetailRepository.findByPlaceCategoryDetailCode(category)
                    .orElse(null);
            if (detail == null) {
                placeCategoryDetailList.addAll(placeCategoryDetailRepository.findByPlaceCategoryDetailCodeStartingWith(category + "-"));
                count = 15; // 카테고리 검색은 15개정도
            } else {
                placeCategoryDetailList.add(detail);
                if (detail.getPlaceCategoryDetailCode().equals("food-korean")) {
                    count = 10; // 세부 카테고리 검색은 한식은 10개만
                } else {
                    count = 30; // 나머지는 30개 정도
                }
            }

            for (PlaceCategoryDetail categoryDetail : placeCategoryDetailList) {

                if (categoryDetail.getContentTypeId() != null) {
                    // TourAPI로 장소 검색
                    List<TourApiPlaceResponse> tourApiPlaceList = tourApiMapService.getPlaceList(longitude, latitude, categoryDetail.getContentTypeId(), count, categoryDetail.getPlaceCategoryDetailCode());
                    // Google Places API로 장소에 대해 평점 붙이기
                    if (!tourApiPlaceList.isEmpty()) {
                        List<PlaceResponseDto> placeListWithRating = googleMapService.getPlaceListWithRating(tourApiPlaceList);
                        mergedList.addAll(placeListWithRating);
                    }
                }

                // 추가 검색 조건
                boolean isRequiredAdditionalSearch = categoryDetail.getAdditionalSearch();

                // 추가 검색 조건에 속한다면 Google Places API의 nearbysearch로 5개 추가 검색
                if (isRequiredAdditionalSearch) {
                    List<PlaceResponseDto> additionalPlace = googleMapService.getPlaceListByLocation(longitude, latitude, categoryDetail.getSearchType(), 5);
                    mergedList.addAll(additionalPlace);
                }
            }
        } else { // 전체 검색을 선택한 경우
            // TourAPI로 10개 장소 검색
            List<TourApiPlaceResponse> tourApiPlaceList = tourApiMapService.getPlaceList(longitude, latitude, null, 10, null);
            // Google Places API로 10개 장소에 대해 평점 붙이기
            if (!tourApiPlaceList.isEmpty()) {
                List<PlaceResponseDto> placeListWithRating = googleMapService.getPlaceListWithRating(tourApiPlaceList);
                mergedList.addAll(placeListWithRating);
            }

            // Google Places API의 nearbysearch로 2개씩 추가 검색
            for (String searchType : placeCategoryDetailRepository.findDistinctSearchTypes()) {
                List<PlaceResponseDto> additionalPlace = googleMapService.getPlaceListByLocation(longitude, latitude, searchType, 2);
                mergedList.addAll(additionalPlace);
            }
        }

        switch (sort) {
            case "title_dsc":
                mergedList.sort(Comparator.comparing(PlaceResponseDto::getName).reversed());
                break;
            case "rating_asc":
                mergedList.sort(Comparator.comparing(dto -> ParseUtil.parseDoubleSafe(dto.getRating())));
                break;
            case "rating_dsc":
                mergedList.sort(Comparator.comparing((PlaceResponseDto dto) -> ParseUtil.parseDoubleSafe(dto.getRating())).reversed());
                break;
            case "user_ratings_total_asc":
                mergedList.sort(Comparator.comparing(dto -> ParseUtil.parseIntSafe(dto.getUserRatingsTotal())));
                break;
            case "user_ratings_total_dsc":
                mergedList.sort(Comparator.comparing((PlaceResponseDto dto) -> ParseUtil.parseIntSafe(dto.getUserRatingsTotal())).reversed());
                break;
            case "title_asc":
            default:
                mergedList.sort(Comparator.comparing(PlaceResponseDto::getName));
        }

        return mergedList;
    }

    // 장소명 자동완성
    public List<AddressAutocompleteDto> getAddressAutocomplete(String keyword) {
        AddressFromKeywordResponse response = kakaoApiService.getAddressFromKeyword(keyword);
        return AddressAutocompleteDto.fromKakaoApiResponse(response);
    }
}

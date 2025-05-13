package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.api.kakao.service.KakaoApiService;
import com.capstone.meetingmap.map.dto.*;
import com.capstone.meetingmap.map.entity.ContentType;
import com.capstone.meetingmap.map.repository.ContentTypeRepository;
import com.capstone.meetingmap.util.ParseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapService {

    private final TourApiMapService tourApiMapService;
    private final GoogleMapService googleMapService;
    private final ContentTypeRepository contentTypeRepository;
    private final KakaoApiService kakaoApiService;

    public MapService(TourApiMapService tourApiMapService, GoogleMapService googleMapService, ContentTypeRepository contentTypeRepository, KakaoApiService kakaoApiService) {
        this.tourApiMapService = tourApiMapService;
        this.googleMapService = googleMapService;
        this.contentTypeRepository = contentTypeRepository;
        this.kakaoApiService = kakaoApiService;
    }

    // TourAPI 결과와 Google Places API 결과를 합쳐서 출력
    public List<PlaceResponseDto> getAllPlaces(String sort, String sigunguCode, String latitude, String longitude, String typeCode, String cat1, String cat2, String cat3) {
        ContentType contentType;
        if (typeCode != null) {
            contentType = contentTypeRepository.findById(Integer.parseInt(typeCode))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "타입 코드를 찾을 수 없습니다"));
        } else contentType = null;

        List<PlaceResponseDto> mergedList = new ArrayList<>();

        // TourAPI로 10개 장소 검색
        List<PlaceResponse> tourApiPlaceList = tourApiMapService.getPlaceList(sigunguCode, longitude, latitude, (contentType != null ? contentType.getContentTypeId() : null), 10, cat1, cat2, cat3);
        System.out.println(tourApiPlaceList);
        // Google Places API로 10개 장소에 대해 평점 붙이기
        if (!tourApiPlaceList.isEmpty()) {
            System.out.println(tourApiPlaceList.get(0).getTitle());
            List<PlaceResponseDto> placeListWithRating = googleMapService.getPlaceListWithRating(tourApiPlaceList);
            mergedList.addAll(placeListWithRating);
        }

        // 추가 검색 조건
        boolean isRequiredAdditionalSearch = ((cat1 == null) ||
                (cat1.equals("A02") && cat2 == null) ||
                (cat1.equals("A02") && cat2.equals("A0202") && cat3 == null) ||
                (cat1.equals("A02") && cat2.equals("A0202") && cat3.equals("A02020200")) ||
                (cat1.equals("A04") && cat2 == null) ||
                (cat1.equals("A04") && cat2.equals("A0401") && cat3 == null) ||
                (cat1.equals("A04") && cat2.equals("A0401") && cat3.equals("A040101000")) ||
                (cat1.equals("A05") && cat2 == null) ||
                (cat1.equals("A05") && cat2.equals("A0502") && cat3 == null) ||
                (cat1.equals("A05") && cat2.equals("A0502") && cat3.equals("A050200900")));

        // 지역 기반 검색이고 추가 검색 조건에 속한다면 Google Places API의 textsearch로 3개씩 추가로 검색
        if (sigunguCode != null && !sigunguCode.isEmpty() && isRequiredAdditionalSearch) {
            List<PlaceResponseDto> additionalPlace = googleMapService.getPlaceListByArea(sigunguCode, cat1, cat2, cat3, 3);
            mergedList.addAll(additionalPlace);
        }

        // 위치 기반 검색이고 추가 검색 조건에 속한다면 Google Places API의 nearbysearch로 5개 추가 검색
        if (latitude != null && longitude != null && isRequiredAdditionalSearch) {
            List<PlaceResponseDto> additionalPlace = googleMapService.getPlaceListByLocation(longitude, latitude, cat1, cat2, cat3, 5);
            mergedList.addAll(additionalPlace);
        }

        switch (sort) {
            case "title_dsc":
                mergedList.sort(Comparator.comparing(PlaceResponseDto::getTitle).reversed());
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
                mergedList.sort(Comparator.comparing(PlaceResponseDto::getTitle));
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

    // typeCode를 반환
    public List<CodeResponseDto> getTypeCodes() {
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        return contentTypeList.stream()
                .map(CodeResponseDto::fromContentType)
                .collect(Collectors.toList());
    }

    // 장소명 자동완성
    public List<AddressAutocompleteDto> getAddressAutocomplete(String keyword) {
        AddressFromKeywordResponse response = kakaoApiService.getAddressFromKeyword(keyword);
        return AddressAutocompleteDto.fromKakaoApiResponse(response);
    }
}

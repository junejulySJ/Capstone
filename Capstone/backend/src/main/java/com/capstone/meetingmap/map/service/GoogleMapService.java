package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.api.google.properties.GoogleApiProperties;
import com.capstone.meetingmap.api.google.service.GoogleApiService;
import com.capstone.meetingmap.map.dto.PlaceResponseDto;
import com.capstone.meetingmap.map.dto.googleapi.GoogleApiResponse;
import com.capstone.meetingmap.map.dto.googleapi.GooglePlaceResult;
import com.capstone.meetingmap.map.repository.PlaceCategoryDetailRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class GoogleMapService {

    private final WebClient webClient;
    private final GoogleApiProperties googleApiProperties;
    private final PlaceCategoryDetailRepository placeCategoryDetailRepository;
    private final GoogleApiService googleApiService;

    public GoogleMapService(GoogleApiProperties googleApiProperties, PlaceCategoryDetailRepository placeCategoryDetailRepository, GoogleApiService googleApiService) {
        this.googleApiProperties = googleApiProperties;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(googleApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .baseUrl(googleApiProperties.getBaseUrl())
                .uriBuilderFactory(factory) // URI 빌더 팩토리 설정
                .build();
        this.placeCategoryDetailRepository = placeCategoryDetailRepository;
        this.googleApiService = googleApiService;
    }

    // Google Places API를 webClient로 호출(변경X)
    private <T> T fetchFromApi(
            String path,
            ParameterizedTypeReference<T> responseType,
            Consumer<UriBuilder> uriBuilderConsumer
    ) {
        return webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path(path)
                            .queryParam("key", googleApiProperties.getKey());
                    uriBuilderConsumer.accept(builder);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(responseType)
                .block(); //동기 방식, 비동기는 subscribe()
    }

    // nearbysearch api 호출
    private List<PlaceResponseDto> getPlaceListByNearbySearch(String longitude, String latitude, String searchType, int count) {
        GoogleApiResponse<GooglePlaceResult> response = fetchFromApi(
                "/place/nearbysearch/json",
                new ParameterizedTypeReference<>() {},
                builder -> {
                    builder.queryParam("location", latitude + "," + longitude)
                            .queryParam("radius", "3000")
                            .queryParam("language", "ko")
                            .queryParam("type", searchType);
                }
        );

        if (response == null || response.getResults().isEmpty()) {
            return null;
        }
        return response.getResults()
                .stream()
                .filter(result -> result.getPhotos() != null && !result.getPhotos().isEmpty() &&
                        result.getRating() != null && result.getUser_ratings_total() > 0 &&
                        result.getVicinity() != null && result.getVicinity().contains("구")) // 이미지와 평점, "구"가 들어간 주소가 존재하는 경우만 가져오기
                .map(result -> PlaceResponseDto.fromGooglePlaceResultNearBySearch(
                        result,
                        placeCategoryDetailRepository.findBySearchType(searchType)
                                        .orElseThrow(),
                        googleApiService.getPhoto(result.getPhotos().get(0).getPhoto_reference(), 800)
                ))
                .limit(count)  // 원하는 개수만큼 자르기
                .collect(Collectors.toList());
    }

    // 위치 기반 추가 검색
    public List<PlaceResponseDto> getPlaceListByLocation(String longitude, String latitude, String searchType, int count) {
        return getPlaceListByNearbySearch(longitude, latitude, searchType, count);
    }


}

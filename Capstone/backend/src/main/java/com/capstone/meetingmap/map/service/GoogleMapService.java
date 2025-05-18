package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.GoogleApiProperties;
import com.capstone.meetingmap.map.dto.PlaceResponseDto;
import com.capstone.meetingmap.map.dto.RatingResponse;
import com.capstone.meetingmap.map.dto.TourApiPlaceResponse;
import com.capstone.meetingmap.map.dto.googleapi.GoogleApiResponse;
import com.capstone.meetingmap.map.dto.googleapi.GooglePlaceResult;
import com.capstone.meetingmap.map.repository.PlaceCategoryDetailRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class GoogleMapService {

    private final WebClient webClient;
    private final GoogleApiProperties googleApiProperties;
    private final PlaceCategoryDetailRepository placeCategoryDetailRepository;

    public GoogleMapService(GoogleApiProperties googleApiProperties, PlaceCategoryDetailRepository placeCategoryDetailRepository) {
        this.googleApiProperties = googleApiProperties;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(googleApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .baseUrl(googleApiProperties.getBaseUrl())
                .uriBuilderFactory(factory) // URI 빌더 팩토리 설정
                .build();
        this.placeCategoryDetailRepository = placeCategoryDetailRepository;
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

    // 텍스트로 장소 검색해 평점 추출(변경X)
    private RatingResponse searchPlace(String placeName) {
        GoogleApiResponse<GooglePlaceResult> response = fetchFromApi(
                "/place/textsearch/json",
                new ParameterizedTypeReference<>() {},
                builder -> builder.queryParam("query", URLEncoder.encode(placeName, StandardCharsets.UTF_8))
        );
        if (response == null || response.getResults().isEmpty() || response.getResults().get(0).getRating() == null || response.getResults().get(0).getUser_ratings_total() == null)
            return RatingResponse.builder()
                    .rating(0)
                    .userRatingsTotal(0)
                    .build();
        return RatingResponse.fromGoogleApiResponse(response.getResults().get(0));
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
                        getPlaceImageUrl(result.getPhotos().get(0).getPhoto_reference(), 800)
                ))
                .limit(count)  // 원하는 개수만큼 자르기
                .collect(Collectors.toList());
    }

    // tourAPI 결과에 rating, user_ratings_total 붙여서 반환
    public List<PlaceResponseDto> getPlaceListWithRating(List<TourApiPlaceResponse> tourApiPlaceList) {
        return tourApiPlaceList.stream()
                .map(place -> {
                    RatingResponse ratingResponse = searchPlace(place.getName());
                    return PlaceResponseDto.builder()
                            .address(place.getAddress())
                            .contentId(place.getContentId())
                            .category(place.getCategory())
                            .thumbnail(place.getThumbnail())
                            .latitude(place.getLatitude())
                            .longitude(place.getLongitude())
                            .name(place.getName())
                            .rating(String.valueOf(ratingResponse.getRating()))
                            .userRatingsTotal(String.valueOf(ratingResponse.getUserRatingsTotal()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 위치 기반 추가 검색
    public List<PlaceResponseDto> getPlaceListByLocation(String longitude, String latitude, String searchType, int count) {
        return getPlaceListByNearbySearch(longitude, latitude, searchType, count);
    }

    //구글 지도로 검색한 장소 이미지 주소 제공
    private String getPlaceImageUrl(String photoReference, int maxWidth) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/place/photo")
                        .queryParam("photoreference", photoReference)
                        .queryParam("maxwidth", maxWidth)
                        .queryParam("key", googleApiProperties.getKey())
                        .build())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is3xxRedirection()) {
                        // 리다이렉트된 URL 추출
                        return Mono.just(clientResponse.headers().header("Location").get(0));
                    } else {
                        return Mono.empty();
                    }
                })
                .block();  // 동기 방식으로 처리
    }
}

package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.GoogleApiProperties;
import com.capstone.meetingmap.map.dto.*;
import com.capstone.meetingmap.map.dto.googleapi.GoogleApiResponse;
import com.capstone.meetingmap.map.dto.googleapi.GooglePlaceResult;
import com.capstone.meetingmap.map.mapper.ContentTypeMapper;
import com.capstone.meetingmap.util.AddressUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class GoogleMapService {

    private final WebClient webClient;
    private final GoogleApiProperties googleApiProperties;
    private final TourApiMapService tourApiMapService;

    public GoogleMapService(GoogleApiProperties googleApiProperties, TourApiMapService tourApiMapService) {
        this.googleApiProperties = googleApiProperties;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(googleApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .baseUrl(googleApiProperties.getBaseUrl())
                .uriBuilderFactory(factory) // URI 빌더 팩토리 설정
                .build();
        this.tourApiMapService = tourApiMapService;
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

    // textsearch API 호출
    private List<PlaceResponseDto> getPlaceListByTextSearch(String sigunguCode, Map<String, String> placeKeywords, int count) {
        String sigungu = tourApiMapService.findSigunguNameByCode(sigunguCode);
        List<PlaceResponseDto> allResults = new ArrayList<>();

        for (Map.Entry<String, String> entry : placeKeywords.entrySet()) {
            GoogleApiResponse<GooglePlaceResult> response = fetchFromApi(
                    "/place/textsearch/json",
                    new ParameterizedTypeReference<>() {},
                    builder -> builder.queryParam("query", URLEncoder.encode(sigungu + " " + entry.getValue(), StandardCharsets.UTF_8))
                            .queryParam("language", "ko")
            );

            if (response == null || response.getResults().isEmpty()) continue;

            List<PlaceResponseDto> filtered = response.getResults()
                    .stream()
                    .filter(result -> result.getPhotos() != null && !result.getPhotos().isEmpty() &&
                            result.getRating() != null && result.getUser_ratings_total() > 0 &&
                            result.getTypes() != null && result.getTypes().contains(entry.getKey())) // 이미지와 평점이 존재하고 타입이 특정 값인 경우만 가져오기)
                    .map(result -> PlaceResponseDto.fromGooglePlaceResultTextSearch(
                            result,
                            sigunguCode,
                            ContentTypeMapper.mapTypesToCat(result.getTypes()),
                            getPlaceImageUrl(result.getPhotos().get(0).getPhoto_reference(), 800),
                            getPlaceImageUrl(result.getPhotos().get(0).getPhoto_reference(), 200)
                    ))
                    .limit(count)
                    .toList();

            allResults.addAll(filtered);
        }

        return allResults;
    }

    // nearbysearch api 호출
    private List<PlaceResponseDto> getPlaceListByNearbySearch(String longitude, String latitude, String cat1, String cat2, String cat3, int count) {
        GoogleApiResponse<GooglePlaceResult> response = fetchFromApi(
                "/place/nearbysearch/json",
                new ParameterizedTypeReference<>() {},
                builder -> {
                    builder.queryParam("location", latitude + "," + longitude)
                            .queryParam("radius", "3000")
                            .queryParam("language", "ko");

                    if (cat1 == null || "A02".equals(cat1)) {
                        // cat1이 "A02"일 경우
                        if (cat2 == null && cat3 == null) {
                            builder.queryParam("type", "movie_theater");  // cat2, cat3 모두 null일 경우 영화관
                        } else if ("A0202".equals(cat2) && cat3 == null) {
                            builder.queryParam("type", "movie_theater");  // cat2만 "A0202"일 경우 영화관
                        } else if ("A0202".equals(cat2) && "A02020200".equals(cat3)) {
                            builder.queryParam("type", "movie_theater");  // cat2가 "A0202", cat3가 "A02020200"일 때경우 영화관
                        }
                    }
                    if (cat1 == null || "A04".equals(cat1)) {
                        // cat1이 "A04"일 경우
                        if (cat2 == null && cat3 == null) {
                            builder.queryParam("type", "convenience_store");  // cat2, cat3 모두 null일 경우 편의점
                        } else if ("A0401".equals(cat2) && cat3 == null) {
                            builder.queryParam("type", "convenience_store");  // cat2만 "A0401"일 경우 편의점
                        } else if ("A0401".equals(cat2) && "A040101000".equals(cat3)) {
                            builder.queryParam("type", "convenience_store");  // cat2가 "A0401", cat3가 "A040101000"일 경우 편의점
                        }
                    }
                    if (cat1 == null || "A05".equals(cat1)) {
                        // cat1이 "A05"일 경우
                        if (cat2 == null && cat3 == null) {
                            builder.queryParam("type", "cafe");  // cat2, cat3 모두 null일 경우 카페
                        } else if ("A0502".equals(cat2) && cat3 == null) {
                            builder.queryParam("type", "cafe");  // cat2만 "A0502"일 경우 카페
                        } else if ("A0502".equals(cat2) && "A050200900".equals(cat3)) {
                            builder.queryParam("type", "cafe");  // cat2가 "A0502", cat3가 "A050200900"일 경우 카페
                        }
                    }
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
                        tourApiMapService.findSigunguCodeByName(AddressUtil.extractGu(result.getVicinity())),
                        ContentTypeMapper.mapTypesToCat(result.getTypes()),
                        getPlaceImageUrl(result.getPhotos().get(0).getPhoto_reference(), 800),
                        getPlaceImageUrl(result.getPhotos().get(0).getPhoto_reference(), 200)
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
                            .sigunguCode(place.getSigunguCode())
                            .contentId(place.getContentId())
                            .typeCode(place.getTypeCode())
                            .cat1(place.getCat1())
                            .cat2(place.getCat2())
                            .cat3(place.getCat3())
                            .firstImage(place.getFirstImage())
                            .firstImage2(place.getFirstImage2())
                            .latitude(place.getLatitude())
                            .longitude(place.getLongitude())
                            .name(place.getName())
                            .rating(String.valueOf(ratingResponse.getRating()))
                            .userRatingsTotal(String.valueOf(ratingResponse.getUserRatingsTotal()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 지역구 기반 추가 검색
    public List<PlaceResponseDto> getPlaceListByArea(String sigunguCode, String cat1, String cat2, String cat3, int count) {

        Map<String, String> placeKeywords = new HashMap<>();

        if ("A02".equals(cat1)) {
            // cat1이 "A02"일 경우
            if (cat2 == null && cat3 == null) {
                placeKeywords.put("movie_theater", "영화관");  // cat2, cat3 모두 null일 경우 영화관
            } else if ("A0202".equals(cat2) && cat3 == null) {
                placeKeywords.put("movie_theater", "영화관");  // cat2만 "A0202"일 경우 영화관
            } else if ("A0202".equals(cat2) && "A02020200".equals(cat3)) {
                placeKeywords.put("movie_theater", "영화관");  // cat2가 "A0202", cat3가 "A02020200"일 때경우 영화관
            }
        } else if ("A04".equals(cat1)) {
            // cat1이 "A04"일 경우
            if (cat2 == null && cat3 == null) {
                placeKeywords.put("convenience_store", "편의점");  // cat2, cat3 모두 null일 경우 편의점
            } else if ("A0401".equals(cat2) && cat3 == null) {
                placeKeywords.put("convenience_store", "편의점");  // cat2만 "A0401"일 경우 편의점
            } else if ("A0401".equals(cat2) && "A040101000".equals(cat3)) {
                placeKeywords.put("convenience_store", "편의점");  // cat2가 "A0401", cat3가 "A040101000"일 경우 편의점
            }
        } else if ("A05".equals(cat1)) {
            // cat1이 "A05"일 경우
            if (cat2 == null && cat3 == null) {
                placeKeywords.put("cafe", "카페");  // cat2, cat3 모두 null일 경우 카페
            } else if ("A0502".equals(cat2) && cat3 == null) {
                placeKeywords.put("cafe", "카페");  // cat2만 "A0502"일 경우 카페
            } else if ("A0502".equals(cat2) && "A050200900".equals(cat3)) {
                placeKeywords.put("cafe", "카페");  // cat2가 "A0502", cat3가 "A050200900"일 경우 카페
            }
        }

        return getPlaceListByTextSearch(sigunguCode, placeKeywords, count);
    }

    // 위치 기반 추가 검색
    public List<PlaceResponseDto> getPlaceListByLocation(String longitude, String latitude, String cat1, String cat2, String cat3, int count) {
        return getPlaceListByNearbySearch(longitude, latitude, cat1, cat2, cat3, count);
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

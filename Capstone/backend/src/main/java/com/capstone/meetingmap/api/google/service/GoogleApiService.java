package com.capstone.meetingmap.api.google.service;

import com.capstone.meetingmap.map.dto.RatingResponse;
import com.capstone.meetingmap.map.dto.googleapi.GoogleApiResponse;
import com.capstone.meetingmap.map.dto.googleapi.GooglePlaceResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

@Service
public class GoogleApiService {
    private final RestClient googleRestClient;

    public GoogleApiService(@Qualifier("googleRestClient") RestClient googleRestClient) {
        this.googleRestClient = googleRestClient;
    }

    // 텍스트로 장소 검색 api 호출
    private GoogleApiResponse<GooglePlaceResult> getTextSearch(String placeName) {
        return googleRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/place/textsearch/json")
                        .queryParam("query", placeName)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    // 장소로 이미지 검색 api 호출
    public String getPhoto(String photoReference, int maxWidth) {
        return googleRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/place/photo")
                        .queryParam("photoreference", photoReference)
                        .queryParam("maxwidth", maxWidth)
                        .build())
                .exchange((request, response) -> {
                    if (response.getStatusCode().is3xxRedirection()) {
                        return Mono.justOrEmpty(response.getHeaders().getFirst("Location"));
                    } else {
                        return Mono.<String>empty();
                    }
                })
                .block();
    }

    // 텍스트로 장소 검색해 평점 추출
    public RatingResponse searchPlace(String placeName) {
        GoogleApiResponse<GooglePlaceResult> response = getTextSearch(placeName);
        if (response == null || response.getResults().isEmpty() || response.getResults().get(0).getRating() == null || response.getResults().get(0).getUser_ratings_total() == null)
            return RatingResponse.builder()
                    .rating(0)
                    .userRatingsTotal(0)
                    .build();
        return RatingResponse.fromGoogleApiResponse(response.getResults().get(0));
    }
}

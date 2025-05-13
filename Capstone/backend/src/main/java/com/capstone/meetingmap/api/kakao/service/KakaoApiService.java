package com.capstone.meetingmap.api.kakao.service;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class KakaoApiService {
    private final RestClient kakaoRestClient;

    public KakaoApiService(RestClient kakaoRestClient) {
        this.kakaoRestClient = kakaoRestClient;
    }

    // 키워드로 장소 검색 api 호출
    public AddressFromKeywordResponse getAddressFromKeyword(String query) {
        return kakaoRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", query)
                        .queryParam("size", 10)
                        .build())
                .retrieve()
                .body(AddressFromKeywordResponse.class);
    }
}

package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.api.kakao.properties.KakaoApiProperties;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoAddressSearchResponse;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoCoordinateSearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class KakaoMapService {
    private final WebClient webClient;
    private final KakaoApiProperties kakaoApiProperties;

    public KakaoMapService(KakaoApiProperties kakaoApiProperties) {
        this.kakaoApiProperties = kakaoApiProperties;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(kakaoApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        this.webClient = WebClient.builder()
                .baseUrl(kakaoApiProperties.getBaseUrl())
                .uriBuilderFactory(factory)
                .build();
    }

    // 좌표를 주소로 변환하는 카카오 api 요청
    public List<String> getAreaFromCoordinate(String longitude, String latitude) {
        KakaoCoordinateSearchResponse response = getAddressFromCoordinate(longitude, latitude);
        String area = response.getDocuments().get(0).getRoad_address().getRegion_1depth_name();
        String sigungu = response.getDocuments().get(0).getRoad_address().getRegion_2depth_name();
        return List.of(area, sigungu);
    }

    // 주소를 좌표로 변환하는 카카오 api 요청
    private KakaoAddressSearchResponse getCoordinateFromRegion(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", encodedAddress)
                            .build())
                    .header("Authorization", "KakaoAK " + kakaoApiProperties.getRestApiKey())
                    .retrieve()
                    .bodyToMono(KakaoAddressSearchResponse.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            }
    }

    // 좌표를 주소로 변환하는 카카오 api 요청
    private KakaoCoordinateSearchResponse getAddressFromCoordinate(String x, String y) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/geo/coord2address.json")
                            .queryParam("x", x)
                            .queryParam("y", y)
                            .build())
                    .header("Authorization", "KakaoAK " + kakaoApiProperties.getRestApiKey())
                    .retrieve()
                    .bodyToMono(KakaoCoordinateSearchResponse.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

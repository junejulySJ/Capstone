package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.KakaoApiProperties;
import com.capstone.meetingmap.map.dto.KakaoAddressSearchResponse;
import com.capstone.meetingmap.map.dto.XYCoordinate;
import com.capstone.meetingmap.map.dto.XYDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    public XYDto getMiddlePoint(List<String> addresses) {
        // 각 주소에 대한 x, y 좌표를 저장할 리스트
        List<Double> xCoordinates = new ArrayList<>();
        List<Double> yCoordinates = new ArrayList<>();
        List<XYCoordinate> coordinates = new ArrayList<>();

        // 각 주소에 대해 좌표 검색 후 리스트에 저장
        for (String address : addresses) {
            KakaoAddressSearchResponse response = getKakaoAddressSearch(address);
            Double x = Double.parseDouble(response.getDocuments().get(0).getX());
            Double y = Double.parseDouble(response.getDocuments().get(0).getY());

            xCoordinates.add(x);
            yCoordinates.add(y);
            coordinates.add(new XYCoordinate(String.valueOf(x), String.valueOf(y)));
        }

        // x와 y의 평균을 계산하여 중간점 구하기
        Double middleX = xCoordinates.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        Double middleY = yCoordinates.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        // XYDto 반환
        return XYDto.builder()
                .coodinates(coordinates)
                .middleX(middleX)
                .middleY(middleY)
                .build();
    }

    private KakaoAddressSearchResponse getKakaoAddressSearch(String address) {
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
}

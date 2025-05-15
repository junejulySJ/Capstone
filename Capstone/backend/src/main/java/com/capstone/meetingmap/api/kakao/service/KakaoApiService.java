package com.capstone.meetingmap.api.kakao.service;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoAddressSearchResponse;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoCoordinateSearchResponse;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    // 주소를 좌표로 변환하는 카카오 api 호출
    public KakaoAddressSearchResponse getCoordinateFromRegion(String address) {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        return kakaoRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", encodedAddress)
                        .build())
                .retrieve()
                .body(KakaoAddressSearchResponse.class);
    }

    // 좌표를 주소로 변환하는 카카오 api 요청
    public KakaoCoordinateSearchResponse getAddressFromCoordinate(double x, double y) {
        return kakaoRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/geo/coord2address.json")
                        .queryParam("x", String.valueOf(x))
                        .queryParam("y", String.valueOf(y))
                        .build())
                .retrieve()
                .body(KakaoCoordinateSearchResponse.class);
    }

    // 좌표를 주소로 변환하는 카카오 api 요청
    public List<String> getAreaFromCoordinate(double longitude, double latitude) {
        KakaoCoordinateSearchResponse response = getAddressFromCoordinate(longitude, latitude);
        String area = response.getDocuments().get(0).getRoad_address().getRegion_1depth_name();
        String sigungu = response.getDocuments().get(0).getRoad_address().getRegion_2depth_name();
        return List.of(area, sigungu);
    }

    // search=middle-point에 대한 알고리즘을 사용하기 위한 변환
    public List<Coordinate> getCoordList(List<String> names) {
        List<Coordinate> coordList = new ArrayList<>();
        // 각 주소에 대해 좌표 검색 후 리스트에 저장
        for (String name : names) {
            AddressFromKeywordResponse response = getAddressFromKeyword(name);
            coordList.add(new Coordinate(Double.parseDouble(response.getDocuments().get(0).getX()), Double.parseDouble(response.getDocuments().get(0).getY())));
        }
        return coordList;
    }
}

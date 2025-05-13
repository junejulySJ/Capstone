package com.capstone.meetingmap.api.kakao.service;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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

package com.capstone.meetingmap.api.kakao.service;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.api.kakao.dto.PointCoord;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoAddressSearchResponse;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoCoordinateSearchResponse;
import com.capstone.meetingmap.util.AddressUtil;
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

    // 주소를 좌표로 변환하는 카카오 api 호출
    public KakaoAddressSearchResponse getCoordinateFromRegion(String address) {
        return kakaoRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
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
            PointCoord pointCoord = getPointCoord(name);
            coordList.add(new Coordinate(Double.parseDouble(pointCoord.getLon()), Double.parseDouble(pointCoord.getLat())));
        }
        return coordList;
    }

    // 장소명인지 주소인지 검사해 doc, lat, lon 반환
    public PointCoord getPointCoord(String name) {
        // 주소인지 여부 확인
        boolean isAddress = AddressUtil.isAddressQuery(name);

        // 그에 맞는 API 호출
        Object response = isAddress ? getCoordinateFromRegion(name) : getAddressFromKeyword(name);

        return PointCoord.fromResponse(response);
    }
}

package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.KakaoApiProperties;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoAddressSearchResponse;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoCoordinateSearchResponse;
import com.capstone.meetingmap.map.dto.XYCoordinate;
import com.capstone.meetingmap.map.dto.XYDto;
import com.capstone.meetingmap.util.ClampUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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

    // search=address에 대한 좌표->주소 변환
    public KakaoAddressSearchResponse getPoint(String address) {
        return getCoordinateFromRegion(address);
    }

    // search=middle-point에 대한 알고리즘(좌표 평균 계산)
    public XYDto getMiddlePoint(List<String> addresses) {
        // 각 주소에 대한 x, y 좌표를 저장할 리스트
        List<Double> xCoordinates = new ArrayList<>();
        List<Double> yCoordinates = new ArrayList<>();
        List<XYCoordinate> coordinates = new ArrayList<>();

        // 각 주소에 대해 좌표 검색 후 리스트에 저장
        for (String address : addresses) {
            KakaoAddressSearchResponse response = getCoordinateFromRegion(address);
            Double x = Double.parseDouble(response.getDocuments().get(0).getX());
            Double y = Double.parseDouble(response.getDocuments().get(0).getY());

            xCoordinates.add(x);
            yCoordinates.add(y);
            coordinates.add(new XYCoordinate(String.valueOf(x), String.valueOf(y)));
        }

        // x와 y의 평균을 계산하여 중간점 구하기
        double middleX = xCoordinates.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double middleY = yCoordinates.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        // 좌표 클램핑(경계 조정)
        Point clampedMiddlePoint = ClampUtil.clampPoint(new GeometryFactory().createPoint(new Coordinate(middleX, middleY)));

        // XYDto 반환
        return XYDto.builder()
                .coordinates(coordinates)
                .middleX(clampedMiddlePoint.getX())
                .middleY(clampedMiddlePoint.getY())
                .build();
    }

    // search=middle-point2에 대한 알고리즘을 사용하기 위한 변환
    public List<Coordinate> getCoordList(List<String> addresses) {
        List<Coordinate> coordList = new ArrayList<>();
        // 각 주소에 대해 좌표 검색 후 리스트에 저장
        for (String address : addresses) {
            KakaoAddressSearchResponse response = getCoordinateFromRegion(address);
            coordList.add(new Coordinate(Double.parseDouble(response.getDocuments().get(0).getX()), Double.parseDouble(response.getDocuments().get(0).getY())));
        }
        return coordList;
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

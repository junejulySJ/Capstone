package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.TourApiProperties;
import com.capstone.meetingmap.map.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class MapService {

    private final WebClient webClient;
    private final TourApiProperties tourApiProperties;

    public MapService(TourApiProperties tourApiProperties) {
        this.tourApiProperties = tourApiProperties;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(tourApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .baseUrl(tourApiProperties.getBaseUrl())
                .uriBuilderFactory(factory) // URI 빌더 팩토리 설정
                .build();
    }

    private <T> CommonTourApiResponse<T> fetchFromApi(
            String path,
            ParameterizedTypeReference<CommonTourApiResponse<T>> responseType,
            Consumer<UriBuilder> uriBuilderConsumer
    ) {
        return webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path(path)
                            .queryParam("serviceKey", tourApiProperties.getKey())
                            .queryParam("MobileOS", "ETC")
                            .queryParam("MobileApp", "MeetingMap")
                            .queryParam("_type", "json"); //json으로 받아오기

                    uriBuilderConsumer.accept(builder);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(responseType)
                .block(); //동기 방식, 비동기는 subscribe()
    }


    public List<CodeResponseDto> getRegionCodes(String areaCode) {

        CommonTourApiResponse<CodeItem> response = fetchFromApi(
                "/areaCode1",
                new ParameterizedTypeReference<>() {},
                builder -> {
                    builder.queryParam("numOfRows", 20)
                            .queryParam("pageNo", 1);
                    if (areaCode != null && !areaCode.isEmpty()) {
                        builder.queryParam("areaCode", areaCode); //areaCode가 비지 않았다면 파라미터에 추가
                    }
                }
        );

        if (response == null
                || response.getResponse() == null
                || response.getResponse().getBody() == null
                || response.getResponse().getBody().getItems() == null) {
            return Collections.emptyList();
        }
        return response.getResponse().getBody().getItems().getItem().stream()
                .map(item -> CodeResponseDto.builder()
                        .code(item.getCode())
                        .name(item.getName())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<AreaBasedListResponseDto> getMap(String areaCode, String sigunguCode, String contentTypeId) {
        CommonTourApiResponse<AreaBasedListItem> response;
        if (contentTypeId.equals("0")) { //전체를 선택
            response = fetchFromApi(
                    "/areaBasedList1",
                    new ParameterizedTypeReference<>() {},
                    builder -> builder.queryParam("numOfRows", 10)
                            .queryParam("pageNo", 1)
                            .queryParam("areaCode", areaCode) //지역코드 적용
                            .queryParam("sigunguCode", sigunguCode) //시군구코드 적용
                            .queryParam("arrange", "Q")
                            .queryParam("listYN", "Y") //Y=목록, N=개수
            );
        } else {
            response = fetchFromApi(
                    "/areaBasedList1",
                    new ParameterizedTypeReference<>() {},
                    builder -> builder.queryParam("numOfRows", 10)
                            .queryParam("pageNo", 1)
                            .queryParam("areaCode", areaCode) //지역코드 적용
                            .queryParam("sigunguCode", sigunguCode) //시군구코드 적용
                            .queryParam("contentTypeId", contentTypeId) //타입 ID 적용(관광지: 12, 숙박: 32, 쇼핑: 38, 음식점: 39, ...)
                            .queryParam("arrange", "Q")
                            .queryParam("listYN", "Y") //Y=목록, N=개수
            );
        }

        if (response == null
                || response.getResponse() == null
                || response.getResponse().getBody() == null
                || response.getResponse().getBody().getItems() == null) {
            return Collections.emptyList();
        }
        return response.getResponse().getBody().getItems().getItem().stream()
                .map(item -> AreaBasedListResponseDto.builder()
                        .addr(item.getAddr1() + " " + item.getAddr2())
                        .contentid(item.getContentid())
                        .contenttypeid(item.getContenttypeid())
                        .createdtime(item.getCreatedtime())
                        .firstimage(item.getFirstimage())
                        .firstimage2(item.getFirstimage2())
                        .mapx(item.getMapx())
                        .mapy(item.getMapy())
                        .mlevel(item.getMlevel())
                        .modifiedtime(item.getModifiedtime())
                        .tel(item.getTel())
                        .title(item.getTitle())
                        .zipcode(item.getZipcode())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public DetailCommonResponseDto getPlaceDetail(String contentId) {
        CommonTourApiResponse<DetailCommonItem> response = fetchFromApi(
                "/detailCommon1",
                new ParameterizedTypeReference<>() {},
                builder -> builder.queryParam("numOfRows", 10)
                        .queryParam("pageNo", 1)
                        .queryParam("contentId", contentId) //콘텐츠ID 적용
                        .queryParam("defaultYN", "Y")
                        .queryParam("firstImageYN", "Y")
                        .queryParam("addrinfoYN", "Y")
                        .queryParam("overviewYN", "Y")
            );
        if (response == null
                || response.getResponse() == null
                || response.getResponse().getBody() == null
                || response.getResponse().getBody().getItems() == null) {
            throw new RuntimeException("콘텐츠ID가 잘못되었습니다.");
        }

        DetailCommonItem item = response.getResponse().getBody().getItems().getItem().get(0);

        return DetailCommonResponseDto.builder()
                .contentid(item.getContentid())
                .contenttypeid(item.getContenttypeid())
                .createdtime(item.getCreatedtime())
                .homepage(item.getHomepage())
                .modifiedtime(item.getModifiedtime())
                .tel(item.getTel())
                .telname(item.getTelname())
                .title(item.getTitle())
                .firstimage(item.getFirstimage())
                .firstimage2(item.getFirstimage2())
                .addr(item.getAddr1() + " " + item.getAddr2())
                .zipcode(item.getZipcode())
                .overview(item.getOverview())
                .build();
    }
}

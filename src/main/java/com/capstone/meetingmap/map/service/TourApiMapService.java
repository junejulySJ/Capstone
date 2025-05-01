package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.TourApiProperties;
import com.capstone.meetingmap.map.dto.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TourApiMapService {

    private final WebClient webClient;
    private final TourApiProperties tourApiProperties;

    public TourApiMapService(TourApiProperties tourApiProperties) {
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

        String rawJson = webClient.get()
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
                .bodyToMono(String.class)
                .block(); //동기 방식, 비동기는 subscribe()
        // 이후 ObjectMapper로 수동 파싱
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(rawJson);

            // items가 빈 문자열인지 체크
            JsonNode itemsNode = root.path("response").path("body").path("items");
            if (itemsNode.isTextual() && itemsNode.asText().isEmpty()) {
                // 빈 리스트를 가진 객체 생성해서 반환
                CommonTourApiResponse<T> emptyResponse = new CommonTourApiResponse<>();
                CommonTourApiResponse.Response<T> res = new CommonTourApiResponse.Response<>();
                CommonTourApiResponse.Body<T> body = new CommonTourApiResponse.Body<>();
                body.setItems(new CommonTourApiResponse.Items<>());
                body.getItems().setItem(Collections.emptyList());
                res.setBody(body);
                emptyResponse.setResponse(res);
                return emptyResponse;
            }
            // 정상 파싱
            JavaType javaType = mapper.getTypeFactory().constructType(responseType.getType());
            return mapper.readValue(rawJson, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<CodeResponseDto> getRegionCodes(String areaCode) {

        CommonTourApiResponse<CodeItem> response = fetchFromApi(
                "/areaCode1",
                new ParameterizedTypeReference<>() {},
                builder -> {
                    builder.queryParam("numOfRows", 30)
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

    public MiddlePointResponseDto<List<PlaceResponseDto>> getPlacesByMiddlePoint(List<String> addresses, XYDto xyDto, List<PlaceResponseDto> placeDto) {
        return MiddlePointResponseDto.<List<PlaceResponseDto>>builder()
                .addresses(addresses)
                .coordinates(xyDto.getCoordinates())
                .middleX(String.valueOf(xyDto.getMiddleX()))
                .middleY(String.valueOf(xyDto.getMiddleY()))
                .list(placeDto)
                .build();
    }

    public List<PlaceResponseDto> getAreaBasedList(String areaCode, String sigunguCode, String contentTypeId, int count, String cat1, String cat2, String cat3) {
        return searchPlaces("/areaBasedList1", areaCode, sigunguCode, contentTypeId, null, count,
                new ParameterizedTypeReference<>() {}, PlaceResponseDto::fromAreaBasedListItem, cat1, cat2, cat3);
    }

    public List<PlaceResponseDto> getKeywordBasedList(String areaCode, String sigunguCode, String contentTypeId, String keyword, int count, String cat1, String cat2, String cat3) {
        String nullableKeywords = (keyword == null ? null : URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        return searchPlaces("/searchKeyword1", areaCode, sigunguCode, contentTypeId, nullableKeywords, count,
                new ParameterizedTypeReference<>() {}, PlaceResponseDto::fromKeywordBasedListItem, cat1, cat2, cat3);
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

    public <T> List<PlaceResponseDto> searchPlaces(
            String path,
            String areaCode,
            String sigunguCode,
            String contentTypeId,
            String keyword,
            int count,
            ParameterizedTypeReference<CommonTourApiResponse<T>> responseType,
            Function<T, PlaceResponseDto> mapper,
            String cat1,
            String cat2,
            String cat3
    ) {
        CommonTourApiResponse<T> response = fetchFromApi(
                path,
                responseType,
                builder -> {
                    builder.queryParam("numOfRows", count)
                            .queryParam("pageNo", 1)
                            .queryParam("listYN", "Y")
                            .queryParam("arrange", "Q");
                    if (areaCode != null && !areaCode.isBlank()) {
                        builder.queryParam("areaCode", areaCode); //지역코드 적용
                    }
                    if (sigunguCode != null && !sigunguCode.isBlank()) {
                        builder.queryParam("sigunguCode", sigunguCode); //시군구코드 적용
                    }
                    if (contentTypeId != null && !contentTypeId.isBlank()) {
                        builder.queryParam("contentTypeId", contentTypeId); //타입 ID 적용(관광지: 12, 숙박: 32, 쇼핑: 38, 음식점: 39, ...)
                    }
                    if (keyword != null && !keyword.isBlank()) {
                        builder.queryParam("keyword", keyword);
                    }
                    if (cat1 != null && !cat1.isBlank()) {
                        builder.queryParam("cat1", cat1);
                    }
                    if (cat2 != null && !cat2.isBlank()) {
                        builder.queryParam("cat2", cat2);
                    }
                    if (cat3 != null && !cat3.isBlank()) {
                        builder.queryParam("cat3", cat3);
                    }
                }
        );
        if (response == null
                || response.getResponse() == null
                || response.getResponse().getBody() == null
                || response.getResponse().getBody().getItems() == null) {
            return Collections.emptyList();
        }

        return response.getResponse().getBody().getItems().getItem()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    private String findCodeByName(List<CodeResponseDto> list, String name, String errorMsg) {
        return list.stream()
                .filter(dto -> dto.getName().equals(name))
                .map(CodeResponseDto::getCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(errorMsg + ": " + name));
    }

    public String findAreaCodeByName(String name) {
        return findCodeByName(getRegionCodes(null), name, "지역명 없음");
    }

    public String findSigunguCodeByName(String name, String areaCode) {
        return findCodeByName(getRegionCodes(areaCode), name, "시군구명 없음");
    }
}

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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TourApiMapService {

    private final WebClient webClient;
    private final TourApiProperties tourApiProperties;
    private final KakaoMapService kakaoMapService;

    public TourApiMapService(TourApiProperties tourApiProperties, KakaoMapService kakaoMapService) {
        this.tourApiProperties = tourApiProperties;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(tourApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .baseUrl(tourApiProperties.getBaseUrl())
                .uriBuilderFactory(factory) // URI 빌더 팩토리 설정
                .build();
        this.kakaoMapService = kakaoMapService;
    }

    // TourAPI를 webClient로 호출, 공통 쿼리 추가, lists=""일 경우 빈 객체로 변환(변경X)
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

    // webClient로 호출한 response 검증 및 dto로 매핑(변경X)
    private static <T, R> List<R> getResultDtoList(Function<T, R> mapper, CommonTourApiResponse<T> response) {
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

    //지역코드, 시군구코드, 대/중/소분류코드를 fetchFromApi로 검색(변경X)
    private <T> List<CodeResponseDto> searchCodes(
            String path,
            String areaCode,
            String cat1,
            String cat2,
            int count,
            ParameterizedTypeReference<CommonTourApiResponse<T>> responseType,
            Function<T, CodeResponseDto> mapper
    ) {
        CommonTourApiResponse<T> response = fetchFromApi(
                path,
                responseType,
                builder -> {
                    builder.queryParam("numOfRows", count)
                            .queryParam("pageNo", 1);
                    if (areaCode != null && !areaCode.isBlank()) {
                        builder.queryParam("areaCode", areaCode); //지역코드 적용
                    }
                    if (cat1 != null && !cat1.isBlank()) {
                        builder.queryParam("cat1", cat1); //대분류 적용
                    }
                    if (cat2 != null && !cat2.isBlank()) {
                        builder.queryParam("cat2", cat2); //중분류 적용
                    }
                }
        );
        return getResultDtoList(mapper, response);
    }

    //장소 정보를 fetchFromApi로 검색(변경X)
    private <T> List<PlaceResponseDto> searchPlaces(
            String path,
            String areaCode,
            String sigunguCode,
            String mapX,
            String mapY,
            String radius,
            String contentTypeId,
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
                    if (mapX != null && !mapX.isBlank()) {
                        builder.queryParam("mapX", mapX); //x좌표 적용
                    }
                    if (mapY != null && !mapY.isBlank()) {
                        builder.queryParam("mapY", mapY); //y좌표 적용
                    }
                    if (radius != null && !radius.isBlank()) {
                        builder.queryParam("radius", radius); //반경 적용
                    }
                    if (contentTypeId != null && !contentTypeId.isBlank()) {
                        builder.queryParam("contentTypeId", contentTypeId); //타입 ID 적용(관광지: 12, 숙박: 32, 쇼핑: 38, 음식점: 39, ...)
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
        return getResultDtoList(mapper, response);
    }

    //지역코드/시군구코드를 조회
    public List<CodeResponseDto> getRegionCodes(String areaCode) {
        return searchCodes("/areaCode1", areaCode, null, null, 40,
                new ParameterizedTypeReference<>() {}, CodeResponseDto::fromCodeItem);
    }

    //대/중/소분류코드를 조회
    public List<CodeResponseDto> getCategoryCodes(String cat1, String cat2) {
        return searchCodes("/categoryCode1", null, cat1, cat2, 30,
                new ParameterizedTypeReference<>() {}, CodeResponseDto::fromCodeItem);
    }

    //지역/좌표(중간위치 포함)/도착지별 장소 조회
    public List<PlaceResponseDto> getPlaceList(String areaCode, String sigunguCode, String address, String x, String y, String contentTypeId, int count, String cat1, String cat2, String cat3) {
        //지역 기반 장소 조회
        if (areaCode != null && !areaCode.isEmpty()) {
            return searchPlaces("/areaBasedList1", areaCode, sigunguCode, null, null, null, contentTypeId, count,
                    new ParameterizedTypeReference<>() {}, PlaceResponseDto::fromAreaBasedListItem, cat1, cat2, cat3);
        }

        //도착지 기반 장소 조회
        else if (address != null && !address.isEmpty()) {
            KakaoAddressSearchResponse response = kakaoMapService.getPoint(address);
            return searchPlaces("/locationBasedList1", null, null, response.getDocuments().get(0).getX(), response.getDocuments().get(0).getY(), "3000", contentTypeId, count,
                    new ParameterizedTypeReference<>() {}, PlaceResponseDto::fromLocationBasedListItem, null, null, null);
            //이후에 cat1, cat2, cat3를 바탕으로 필터링 필요
            //현재는 cat1, cat2, cat3를 무시하고 출력
        }

        //좌표 기반 장소 조회
        else {
            return searchPlaces("/locationBasedList1", null, null, x, y, "3000", contentTypeId, count,
                    new ParameterizedTypeReference<>() {}, PlaceResponseDto::fromLocationBasedListItem, null, null, null);
            //이후에 cat1, cat2, cat3를 바탕으로 필터링 필요
            //현재는 cat1, cat2, cat3를 무시하고 출력
        }
    }

    //장소 상세 조회(로직 수정 예정)
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

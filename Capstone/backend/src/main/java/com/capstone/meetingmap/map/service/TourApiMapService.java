package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.TourApiProperties;
import com.capstone.meetingmap.map.dto.CodeResponseDto;
import com.capstone.meetingmap.map.dto.DetailCommonResponseDto;
import com.capstone.meetingmap.map.dto.TourApiPlaceResponse;
import com.capstone.meetingmap.map.dto.tourapi.CommonTourApiResponse;
import com.capstone.meetingmap.map.dto.tourapi.DetailCommonItem;
import com.capstone.meetingmap.map.dto.tourapi.LocationBasedListItem;
import com.capstone.meetingmap.map.repository.PlaceCategoryDetailRepository;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
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
    private final PlaceCategoryDetailService placeCategoryDetailService;

    public TourApiMapService(TourApiProperties tourApiProperties, PlaceCategoryDetailRepository placeCategoryDetailRepository, PlaceCategoryDetailService placeCategoryDetailService) {
        this.tourApiProperties = tourApiProperties;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(tourApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .baseUrl(tourApiProperties.getBaseUrl())
                .uriBuilderFactory(factory) // URI 빌더 팩토리 설정
                .build();
        this.placeCategoryDetailService = placeCategoryDetailService;
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
    private static <T> List<TourApiPlaceResponse> getResultDtoList(
            Function<T, TourApiPlaceResponse> mapper,
            CommonTourApiResponse<T> response,
            String category
    ) {
        if (response == null
                || response.getResponse() == null
                || response.getResponse().getBody() == null
                || response.getResponse().getBody().getItems() == null) {
            return Collections.emptyList();
        }
        return response.getResponse().getBody().getItems().getItem()
                .stream()
                .map(mapper)
                .filter(tourApiPlaceResponse -> {       // 필터링 조건 추가
                    // category가 null이 아니고 해당 조건을 만족하는지 체크
                    System.out.println("Category=" + category);
                    System.out.println("tourApiPlaceResponse.getCategory=" + tourApiPlaceResponse.getCategory());
                    if (category != null && !tourApiPlaceResponse.getCategory().contains(category)) {
                        return false; // category가 일치하지 않으면 필터링
                    }
                    return true; // 조건을 만족하면 true
                })
                .collect(Collectors.toList());
    }

    // webClient로 호출한 Coderesponse 검증 및 dto로 매핑(변경X)
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
    private <T> List<TourApiPlaceResponse> searchPlaces(
            String path,
            String areaCode,
            String sigunguCode,
            String mapX,
            String mapY,
            String radius,
            String contentTypeId,
            int count,
            ParameterizedTypeReference<CommonTourApiResponse<T>> responseType,
            Function<T, TourApiPlaceResponse> mapper,
            String cat1,
            String cat2,
            String cat3,
            String category
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
                    if (areaCode != null && !areaCode.isBlank() && cat1 != null && !cat1.isBlank()) {
                        builder.queryParam("cat1", cat1);
                    }
                    if (areaCode != null && !areaCode.isBlank() && cat2 != null && !cat2.isBlank()) {
                        builder.queryParam("cat2", cat2);
                    }
                    if (areaCode != null && !areaCode.isBlank() && cat3 != null && !cat3.isBlank()) {
                        builder.queryParam("cat3", cat3);
                    }
                }
        );
        return getResultDtoList(mapper, response, category);
    }

    //지역코드/시군구코드를 조회
    public List<CodeResponseDto> getRegionCodes() {
        return searchCodes("/areaCode1", "1", null, null, 40,
                new ParameterizedTypeReference<>() {}, CodeResponseDto::fromCodeItem);
    }

    //지역/좌표(중간위치 포함)/도착지별 장소 조회
    public List<TourApiPlaceResponse> getPlaceList(String x, String y, String contentTypeId, int count, String cat1, String cat2, String cat3, String category) {
        //좌표 기반 장소 조회(3km 반경)
        List<TourApiPlaceResponse> places = searchPlaces("/locationBasedList1", null, null, x, y, "3000", contentTypeId, count,
                new ParameterizedTypeReference<CommonTourApiResponse<LocationBasedListItem>>() {},
                item -> TourApiPlaceResponse.fromLocationBasedListItem(item,
                        placeCategoryDetailService.searchPlaceCategoryDetail(item.getContenttypeid(), item.getCat1(), item.getCat2(), item.getCat3())
                ), cat1, cat2, cat3, category);
        return places.stream()
                .filter(place -> !place.getCategory().equals("other")) //contentTypeId가 0일때 제거
                .collect(Collectors.toList());
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "반환값을 찾을 수 없습니다");
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

    // 시군구명으로 시군구 코드 찾기
    public String findSigunguCodeByName(String name) {
        return getRegionCodes()
                .stream()
                .filter(dto -> dto.getName().equals(name))
                .map(CodeResponseDto::getCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("시군구명 없음: " + name));
    }

    // 시군구 코드로 시군구명 찾기
    public String findSigunguNameByCode(String code) {
        return getRegionCodes()
                .stream()
                .filter(dto -> dto.getCode().equals(code))
                .map(CodeResponseDto::getName)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("시군구코드 없음: " + code));
    }
}

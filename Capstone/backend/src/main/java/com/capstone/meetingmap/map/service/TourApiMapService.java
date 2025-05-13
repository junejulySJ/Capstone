package com.capstone.meetingmap.map.service;

import com.capstone.meetingmap.TourApiProperties;
import com.capstone.meetingmap.map.dto.CodeResponseDto;
import com.capstone.meetingmap.map.dto.DetailCommonResponseDto;
import com.capstone.meetingmap.map.dto.TourApiPlaceResponse;
import com.capstone.meetingmap.map.dto.tourapi.AreaBasedListItem;
import com.capstone.meetingmap.map.dto.tourapi.CommonTourApiResponse;
import com.capstone.meetingmap.map.dto.tourapi.DetailCommonItem;
import com.capstone.meetingmap.map.dto.tourapi.LocationBasedListItem;
import com.capstone.meetingmap.map.entity.ContentType;
import com.capstone.meetingmap.map.repository.ContentTypeRepository;
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
    private final ContentTypeRepository contentTypeRepository;

    public TourApiMapService(TourApiProperties tourApiProperties, ContentTypeRepository contentTypeRepository) {
        this.tourApiProperties = tourApiProperties;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(tourApiProperties.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .baseUrl(tourApiProperties.getBaseUrl())
                .uriBuilderFactory(factory) // URI 빌더 팩토리 설정
                .build();
        this.contentTypeRepository = contentTypeRepository;
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
            String cat1, String cat2, String cat3
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
                    // cat1, cat2, cat3이 null이 아니고 해당 조건을 만족하는지 체크
                    if (cat1 != null && !cat1.equals(tourApiPlaceResponse.getCat1())) {
                        return false; // cat1이 일치하지 않으면 필터링
                    }
                    if (cat2 != null && !cat2.equals(tourApiPlaceResponse.getCat2())) {
                        return false; // cat2가 일치하지 않으면 필터링
                    }
                    if (cat3 != null && !cat3.equals(tourApiPlaceResponse.getCat3())) {
                        return false; // cat3이 일치하지 않으면 필터링
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
        return getResultDtoList(mapper, response, cat1, cat2, cat3);
    }

    //지역코드/시군구코드를 조회
    public List<CodeResponseDto> getRegionCodes() {
        return searchCodes("/areaCode1", "1", null, null, 40,
                new ParameterizedTypeReference<>() {}, CodeResponseDto::fromCodeItem);
    }

    //대/중/소분류코드를 조회
    public List<CodeResponseDto> getCategoryCodes(String typeCode, String cat1, String cat2) {
        List<CodeResponseDto> filteredCodes;

        ContentType contentType = contentTypeRepository.findById(Integer.valueOf(typeCode))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "타입 코드를 찾을 수 없습니다"));

        if (cat1 == null) {
            List<CodeResponseDto> allCodes = searchCodes("/categoryCode1", null, null, null, 30,
                    new ParameterizedTypeReference<>() {}, CodeResponseDto::fromCodeItem);
            if (contentType.getContentTypeId().equals("12")) {
                // "A01", "A02"만 필터링
                filteredCodes = allCodes.stream()
                        .filter(code -> "A01".equals(code.getCode()) || "A02".equals(code.getCode()))
                        .toList();
            } else if (contentType.getContentTypeId().equals("14") || contentType.getContentTypeId().equals("15")) {
                // "A02"만 필터링
                filteredCodes = allCodes.stream()
                        .filter(code -> "A02".equals(code.getCode()))
                        .toList();
            } else if (contentType.getContentTypeId().equals("28")) {
                // "A03"만 필터링
                filteredCodes = allCodes.stream()
                        .filter(code -> "A03".equals(code.getCode()))
                        .toList();
            } else if (contentType.getContentTypeId().equals("38")) {
                // "A04"만 필터링
                filteredCodes = allCodes.stream()
                        .filter(code -> "A04".equals(code.getCode()))
                        .toList();
            } else if (contentType.getContentTypeId().equals("39")) {
                // "A05"만 필터링
                filteredCodes = allCodes.stream()
                        .filter(code -> "A05".equals(code.getCode()))
                        .toList();
            } else {
                return Collections.emptyList();
            }
            return filteredCodes;
        } else if (cat2 == null) {
            List<CodeResponseDto> allCodes = searchCodes("/categoryCode1", null, cat1, null, 30,
                    new ParameterizedTypeReference<>() {}, CodeResponseDto::fromCodeItem);
            if (contentType.getContentTypeId().equals("12") && cat1.equals("A02")) {
                // "A0201"~"A0205"만 필터링
                filteredCodes = allCodes.stream()
                        .filter(code -> "A0201".equals(code.getCode()) || "A0202".equals(code.getCode()) || "A0203".equals(code.getCode()) || "A0204".equals(code.getCode()) || "A0205".equals(code.getCode()))
                        .toList();
            } else if (contentType.getContentTypeId().equals("14")) {
                // "A0206"만 필터링
                filteredCodes = allCodes.stream()
                        .filter(code -> "A0206".equals(code.getCode()))
                        .toList();
            } else if (contentType.getContentTypeId().equals("15")) {
                // "A0207", "A0208"만 필터링
                filteredCodes = allCodes.stream()
                        .filter(code -> "A0207".equals(code.getCode()) || "A0208".equals(code.getCode()))
                        .toList();
            } else {
                return allCodes;
            }
            return filteredCodes;
        } else {
            return searchCodes("/categoryCode1", null, cat1, cat2, 30,
                    new ParameterizedTypeReference<>() {}, CodeResponseDto::fromCodeItem);
        }
    }

    //지역/좌표(중간위치 포함)/도착지별 장소 조회
    public List<TourApiPlaceResponse> getPlaceList(String sigunguCode, String x, String y, String contentTypeId, int count, String cat1, String cat2, String cat3) {
        //지역 기반 장소 조회
        if (sigunguCode != null && !sigunguCode.isEmpty()) {
            List<TourApiPlaceResponse> places = searchPlaces("/areaBasedList1", "1", sigunguCode, null, null, null, contentTypeId, count,
                    new ParameterizedTypeReference<CommonTourApiResponse<AreaBasedListItem>>() {},
                    item -> TourApiPlaceResponse.fromAreaBasedListItem(
                            item,
                            contentTypeRepository.findByContentTypeId(item.getContenttypeid()) //contentTypeId가 null일때 전체 검색을 할 때 검색되는 content_type 테이블에 없는 contentTypeId는 제거를 위해 0으로 변환
                                    .orElse(new ContentType(0, item.getContenttypeid(), "미분류"))
                    ), cat1, cat2, cat3);
            return places.stream()
                    .filter(place -> !place.getTypeCode().equals("0")) //contentTypeId가 0일때 제거
                    .collect(Collectors.toList());
        } else { //좌표 기반 장소 조회(3km 반경)
            List<TourApiPlaceResponse> places = searchPlaces("/locationBasedList1", null, null, x, y, "3000", contentTypeId, count,
                    new ParameterizedTypeReference<CommonTourApiResponse<LocationBasedListItem>>() {},
                    item -> TourApiPlaceResponse.fromLocationBasedListItem(
                            item,
                            contentTypeRepository.findByContentTypeId(item.getContenttypeid()) //contentTypeId가 null일때 전체 검색을 할 때 검색되는 content_type 테이블에 없는 contentTypeId는 제거를 위해 0으로 변환
                                    .orElse(new ContentType(0, item.getContenttypeid(), "미분류"))
                    ), cat1, cat2, cat3);
            return places.stream()
                    .filter(place -> !place.getTypeCode().equals("0")) //contentTypeId가 0일때 제거
                    .collect(Collectors.toList());
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

package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.api.kakao.dto.PointCoord;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoAddressSearchResponse;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoCoordinateSearchResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//프론트에 locationBasedList와 각 출발지, 중간 위치를 전달할 dto
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MiddlePointResponseDto<T> {
    private List<CustomPoint> start;
    private CustomPoint2 middlePoint;
    private T list;

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class CustomPoint {
        private String name;
        private String address;
        private String latitude;
        private String longitude;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class CustomPoint2 {
        private String address;
        private String latitude;
        private String longitude;
    }

    // 기존 place dto에 중간지점 dto를 붙여줌
    public static MiddlePointResponseDto<List<PlaceResponseDto>> addMiddlePointResponse(List<PointCoord> pointCoordList, KakaoCoordinateSearchResponse response, XYDto xyDto, List<PlaceResponseDto> placeDto) {
        return MiddlePointResponseDto.<List<PlaceResponseDto>>builder()
                .start(pointCoordList.stream().map(MiddlePointResponseDto::fromPointCoord).toList())
                .middlePoint(CustomPoint2.builder()
                        .address(response.getDocuments().get(0).getAddress().getAddress_name())
                        .latitude(String.valueOf(xyDto.getMiddleX()))
                        .longitude(String.valueOf(xyDto.getMiddleY()))
                        .build()
                )
                .list(placeDto)
                .build();
    }

    public static CustomPoint fromPointCoord(PointCoord pointCoord) {
        Object doc = pointCoord.getDoc();
        String name, address;
        if (doc instanceof KakaoAddressSearchResponse.Document) {
            name = ((KakaoAddressSearchResponse.Document) doc).getRoad_address().getAddress_name() != null ? ((KakaoAddressSearchResponse.Document) doc).getRoad_address().getAddress_name() : ((KakaoAddressSearchResponse.Document) doc).getAddress_name();
            address = ((KakaoAddressSearchResponse.Document) doc).getRoad_address().getAddress_name() != null ? ((KakaoAddressSearchResponse.Document) doc).getRoad_address().getAddress_name() : ((KakaoAddressSearchResponse.Document) doc).getAddress().getAddress_name();
        } else {
            name = ((AddressFromKeywordResponse.Documents) doc).getPlace_name();
            address = ((AddressFromKeywordResponse.Documents) doc).getRoad_address_name();
        }

        return CustomPoint.builder()
                .name(name)
                .address(address)
                .latitude(pointCoord.getLat())
                .longitude(pointCoord.getLon())
                .build();
    }
}

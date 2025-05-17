package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
import com.capstone.meetingmap.map.dto.kakaoapi.KakaoAddressSearchResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//프론트에 locationBasedList와 각 출발지, 도착지를 전달할 dto
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PointResponseDto<T> {
    private CustomPoint start;
    private CustomPoint end;
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

    // 기존 place dto에 지점 dto를 붙여줌
    public static PointResponseDto<List<PlaceResponseDto>> addDestinationResponse(Object startResponse, Object endResponse, List<PlaceResponseDto> placeDto) {
        String startName, startAddress, startLatitude, startLongitude;
        String endName, endAddress, endLatitude, endLongitude;
        if (startResponse instanceof KakaoAddressSearchResponse) {
            KakaoAddressSearchResponse.Document startDoc = ((KakaoAddressSearchResponse) startResponse).getDocuments().get(0);
            startName = (startDoc.getRoad_address().getAddress_name() != null ? startDoc.getRoad_address().getAddress_name() : startDoc.getAddress_name());
            startAddress = (startDoc.getRoad_address().getAddress_name() != null ? startDoc.getRoad_address().getAddress_name() : startDoc.getAddress().getAddress_name());
            startLatitude = ((KakaoAddressSearchResponse) startResponse).getDocuments().get(0).getY();
            startLongitude = ((KakaoAddressSearchResponse) startResponse).getDocuments().get(0).getX();
        } else {
            startName = ((AddressFromKeywordResponse) startResponse).getDocuments().get(0).getPlace_name();
            startAddress = ((AddressFromKeywordResponse) startResponse).getDocuments().get(0).getRoad_address_name();
            startLatitude = ((AddressFromKeywordResponse) startResponse).getDocuments().get(0).getY();
            startLongitude = ((AddressFromKeywordResponse) startResponse).getDocuments().get(0).getX();
        }
        if (endResponse instanceof KakaoAddressSearchResponse) {
            KakaoAddressSearchResponse.Document endDoc = ((KakaoAddressSearchResponse) endResponse).getDocuments().get(0);
            endName = (endDoc.getRoad_address().getAddress_name() != null ? endDoc.getRoad_address().getAddress_name() : endDoc.getAddress_name());
            endAddress = (endDoc.getRoad_address().getAddress_name() != null ? endDoc.getRoad_address().getAddress_name() : endDoc.getAddress().getAddress_name());
            endLatitude = ((KakaoAddressSearchResponse) endResponse).getDocuments().get(0).getY();
            endLongitude = ((KakaoAddressSearchResponse) endResponse).getDocuments().get(0).getX();
        } else {
            endName = ((AddressFromKeywordResponse) endResponse).getDocuments().get(0).getPlace_name();
            endAddress = ((AddressFromKeywordResponse) endResponse).getDocuments().get(0).getRoad_address_name();
            endLatitude = ((AddressFromKeywordResponse) endResponse).getDocuments().get(0).getY();
            endLongitude = ((AddressFromKeywordResponse) endResponse).getDocuments().get(0).getX();
        }

        return PointResponseDto.<List<PlaceResponseDto>>builder()
                .start(CustomPoint.builder()
                        .name(startName)
                        .address(startAddress)
                        .latitude(startLatitude)
                        .longitude(startLongitude)
                        .build())
                .end(CustomPoint.builder()
                        .name(endName)
                        .address(endAddress)
                        .latitude(endLatitude)
                        .longitude(endLongitude)
                        .build())
                .list(placeDto)
                .build();
    }
}

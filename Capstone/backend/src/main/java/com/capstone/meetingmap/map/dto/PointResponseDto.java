package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.api.kakao.dto.AddressFromKeywordResponse;
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
    public static PointResponseDto<List<PlaceResponseDto>> addDestinationResponse(AddressFromKeywordResponse startResponse, AddressFromKeywordResponse endResponse, List<PlaceResponseDto> placeDto) {
        return PointResponseDto.<List<PlaceResponseDto>>builder()
                .start(CustomPoint.builder()
                        .name(startResponse.getDocuments().get(0).getPlace_name())
                        .address(startResponse.getDocuments().get(0).getRoad_address_name())
                        .latitude(startResponse.getDocuments().get(0).getY())
                        .longitude(startResponse.getDocuments().get(0).getX())
                        .build())
                .end(CustomPoint.builder()
                        .name(endResponse.getDocuments().get(0).getPlace_name())
                        .address(endResponse.getDocuments().get(0).getRoad_address_name())
                        .latitude(endResponse.getDocuments().get(0).getY())
                        .longitude(endResponse.getDocuments().get(0).getX())
                        .build())
                .list(placeDto)
                .build();
    }
}

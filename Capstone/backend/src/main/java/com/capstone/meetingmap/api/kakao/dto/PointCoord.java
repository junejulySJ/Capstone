package com.capstone.meetingmap.api.kakao.dto;

import com.capstone.meetingmap.map.dto.kakaoapi.KakaoAddressSearchResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PointCoord {
    Object doc;
    String lat;
    String lon;

    public static PointCoord fromResponse(Object response) {
        if (response instanceof KakaoAddressSearchResponse) { // 주소 검색 결과이면
            return PointCoord.builder()
                    .doc(((KakaoAddressSearchResponse) response).getDocuments().get(0))
                    .lat(((KakaoAddressSearchResponse) response).getDocuments().get(0).getY())
                    .lon(((KakaoAddressSearchResponse) response).getDocuments().get(0).getX())
                    .build();
        } else {  // 장소명 검색 결과이면
            return PointCoord.builder()
                    .doc(((AddressFromKeywordResponse) response).getDocuments().get(0))
                    .lat(((AddressFromKeywordResponse) response).getDocuments().get(0).getY())
                    .lon(((AddressFromKeywordResponse) response).getDocuments().get(0).getX())
                    .build();
        }
    }
}

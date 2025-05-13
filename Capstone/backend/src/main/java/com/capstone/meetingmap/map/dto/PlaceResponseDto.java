package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.map.dto.googleapi.GooglePlaceResult;
import com.capstone.meetingmap.map.mapper.ContentTypeMapper;
import com.capstone.meetingmap.schedule.dto.SelectedPlace;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class PlaceResponseDto {
    private String addr;
    private String areaCode;
    private String sigunguCode;
    private String contentId;
    private String typeCode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String firstImage;
    private String firstImage2;
    private String mapX;
    private String mapY;
    private String title;
    private String rating;
    private String userRatingsTotal;

    // Google Places API textSearch로 가져온 장소 정보를 TourAPI 장소 형태로 변환
    public static PlaceResponseDto fromGooglePlaceResultTextSearch(GooglePlaceResult result, String sigunguCode, ContentTypeMapper.CatCode catCode, String imageUrl1, String imageUrl2) {
        return PlaceResponseDto.builder()
                .addr(result.getFormatted_address())
                .areaCode("1")
                .sigunguCode(sigunguCode)
                .contentId("google_" + result.getPlace_id())
                .typeCode(catCode.typeCode())
                .cat1(catCode.cat1())
                .cat2(catCode.cat2())
                .cat3(catCode.cat3())
                .firstImage(imageUrl1)
                .firstImage2(imageUrl2)
                .mapX(String.valueOf(result.getGeometry().getLocation().getLng()))
                .mapY(String.valueOf(result.getGeometry().getLocation().getLat()))
                .title(result.getName())
                .rating(String.valueOf(result.getRating()))
                .userRatingsTotal(String.valueOf(result.getUser_ratings_total()))
                .build();
    }

    // Google Places API nearbySearch로 가져온 장소 정보를 TourAPI 장소 형태로 변환
    public static PlaceResponseDto fromGooglePlaceResultNearBySearch(GooglePlaceResult result, String sigunguCode, ContentTypeMapper.CatCode catCode, String imageUrl1, String imageUrl2) {
        return PlaceResponseDto.builder()
                .addr(result.getVicinity())
                .areaCode("1")
                .sigunguCode(sigunguCode)
                .contentId("google_" + result.getPlace_id())
                .typeCode(catCode.typeCode())
                .cat1(catCode.cat1())
                .cat2(catCode.cat2())
                .cat3(catCode.cat3())
                .firstImage(imageUrl1)
                .firstImage2(imageUrl2)
                .mapX(String.valueOf(result.getGeometry().getLocation().getLng()))
                .mapY(String.valueOf(result.getGeometry().getLocation().getLat()))
                .title(result.getName())
                .rating(String.valueOf(result.getRating()))
                .userRatingsTotal(String.valueOf(result.getUser_ratings_total()))
                .build();
    }

    // SelectedPlace로 변환
    public SelectedPlace toSelectedPlace(Integer stayMinutesMean) {
        return SelectedPlace.builder()
                .contentId(contentId)
                .address(addr)
                .title(title)
                .latitude(mapY)
                .longitude(mapX)
                .cat3(cat3)
                .stayMinutes(stayMinutesMean)
                .build();
    }
}

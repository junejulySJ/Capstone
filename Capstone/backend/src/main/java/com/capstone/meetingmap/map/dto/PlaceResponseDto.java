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
    private String address;
    private String sigunguCode;
    private String contentId;
    private String typeCode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String firstImage;
    private String firstImage2;
    private String latitude;
    private String longitude;
    private String name;
    private String rating;
    private String userRatingsTotal;

    // Google Places API textSearch로 가져온 장소 정보를 TourAPI 장소 형태로 변환
    public static PlaceResponseDto fromGooglePlaceResultTextSearch(GooglePlaceResult result, String sigunguCode, ContentTypeMapper.CatCode catCode, String imageUrl1, String imageUrl2) {
        return PlaceResponseDto.builder()
                .address(result.getFormatted_address())
                .sigunguCode(sigunguCode)
                .contentId("google_" + result.getPlace_id())
                .typeCode(catCode.typeCode())
                .cat1(catCode.cat1())
                .cat2(catCode.cat2())
                .cat3(catCode.cat3())
                .firstImage(imageUrl1)
                .firstImage2(imageUrl2)
                .latitude(String.valueOf(result.getGeometry().getLocation().getLat()))
                .longitude(String.valueOf(result.getGeometry().getLocation().getLng()))
                .name(result.getName())
                .rating(String.valueOf(result.getRating()))
                .userRatingsTotal(String.valueOf(result.getUser_ratings_total()))
                .build();
    }

    // Google Places API nearbySearch로 가져온 장소 정보를 TourAPI 장소 형태로 변환
    public static PlaceResponseDto fromGooglePlaceResultNearBySearch(GooglePlaceResult result, String sigunguCode, ContentTypeMapper.CatCode catCode, String imageUrl1, String imageUrl2) {
        return PlaceResponseDto.builder()
                .address(result.getVicinity())
                .sigunguCode(sigunguCode)
                .contentId("google_" + result.getPlace_id())
                .typeCode(catCode.typeCode())
                .cat1(catCode.cat1())
                .cat2(catCode.cat2())
                .cat3(catCode.cat3())
                .firstImage(imageUrl1)
                .firstImage2(imageUrl2)
                .latitude(String.valueOf(result.getGeometry().getLocation().getLat()))
                .longitude(String.valueOf(result.getGeometry().getLocation().getLng()))
                .name(result.getName())
                .rating(String.valueOf(result.getRating()))
                .userRatingsTotal(String.valueOf(result.getUser_ratings_total()))
                .build();
    }

    // SelectedPlace로 변환
    public SelectedPlace toSelectedPlace(Integer stayMinutesMean) {
        return SelectedPlace.builder()
                .contentId(contentId)
                .address(address)
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .cat3(cat3)
                .stayMinutes(stayMinutesMean)
                .build();
    }
}

package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.map.dto.googleapi.GooglePlaceResult;
import com.capstone.meetingmap.map.entity.PlaceCategoryDetail;
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
    private String category;
    private String firstImage;
    private String firstImage2;
    private String latitude;
    private String longitude;
    private String name;
    private String rating;
    private String userRatingsTotal;

    // Google Places API nearbySearch로 가져온 장소 정보를 TourAPI 장소 형태로 변환
    public static PlaceResponseDto fromGooglePlaceResultNearBySearch(GooglePlaceResult result, String sigunguCode, PlaceCategoryDetail placeCategoryDetail, String imageUrl1, String imageUrl2) {
        return PlaceResponseDto.builder()
                .address(result.getVicinity())
                .sigunguCode(sigunguCode)
                .contentId("google_" + result.getPlace_id())
                .category(placeCategoryDetail.getPlaceCategoryDetailCode())
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
                .category(category)
                .stayMinutes(stayMinutesMean)
                .build();
    }
}

package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.api.google.dto.DetailCommonItemWithRating;
import com.capstone.meetingmap.api.google.dto.GoogleApiDetailResponse;
import com.capstone.meetingmap.api.google.dto.GooglePlaceDetailResult;
import com.capstone.meetingmap.api.tourapi.dto.DetailImageItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DetailCommonResponseDto {
    private String address;
    private String contentId;
    private List<String> thumbnails;
    private String latitude;
    private String longitude;
    private String name;
    private String rating;
    private String userRatingsTotal;
    private String phoneNumber;
    private String url;

    public static DetailCommonResponseDto fromTourApiPlace(DetailCommonItemWithRating commonItem, List<DetailImageItem> imageItems) {
        return DetailCommonResponseDto.builder()
                .address((commonItem.getAddr1() + " " + commonItem.getAddr2()).trim())
                .contentId(commonItem.getContentId())
                .thumbnails(imageItems.stream().map(DetailImageItem::getOriginimgurl).collect(Collectors.toList()))
                .latitude(commonItem.getMapY())
                .longitude(commonItem.getMapX())
                .name(commonItem.getTitle())
                .rating(String.valueOf(commonItem.getRating()))
                .userRatingsTotal(String.valueOf(commonItem.getUserRatingsTotal()))
                .phoneNumber(commonItem.getTel())
                .url(commonItem.getHomepage())
                .build();
    }

    public static DetailCommonResponseDto fromGoogleApiPlace(GoogleApiDetailResponse<GooglePlaceDetailResult> response, List<String> imageUrls) {
        return DetailCommonResponseDto.builder()
                .address(response.getResult().getVicinity())
                .contentId("google_" + response.getResult().getPlace_id())
                .thumbnails(imageUrls)
                .latitude(String.valueOf(response.getResult().getGeometry().getLocation().getLat()))
                .longitude(String.valueOf(response.getResult().getGeometry().getLocation().getLng()))
                .name(response.getResult().getName())
                .rating(String.valueOf(response.getResult().getRating()))
                .userRatingsTotal(String.valueOf(response.getResult().getUser_ratings_total()))
                .phoneNumber(response.getResult().getFormatted_phone_number())
                .url(response.getResult().getWebsite())
                .build();
    }
}

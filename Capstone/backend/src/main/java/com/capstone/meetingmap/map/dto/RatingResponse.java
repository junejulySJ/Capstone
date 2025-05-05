package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.map.dto.googleapi.GooglePlaceResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingResponse {
    private double rating;
    private int userRatingsTotal;

    public static RatingResponse fromGoogleApiResponse(GooglePlaceResult googlePlaceResult) {
        return RatingResponse.builder()
                .rating(googlePlaceResult.getRating())
                .userRatingsTotal(googlePlaceResult.getUser_ratings_total())
                .build();
    }
}

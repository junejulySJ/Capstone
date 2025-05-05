package com.capstone.meetingmap.map.dto;

import com.capstone.meetingmap.map.dto.tourapi.AreaBasedListItem;
import com.capstone.meetingmap.map.dto.tourapi.LocationBasedListItem;
import com.capstone.meetingmap.map.entity.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlaceResponse {
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

    public static PlaceResponse fromAreaBasedListItem(AreaBasedListItem item, ContentType contentType) {
        return PlaceResponse.builder()
                .addr((item.getAddr1() == null ? "" : item.getAddr1()) + " " + (item.getAddr2() == null ? "" : item.getAddr2()).trim())
                .areaCode(item.getAreacode())
                .sigunguCode(item.getSigungucode())
                .contentId(item.getContentid())
                .typeCode(String.valueOf(contentType.getContentTypeNo()))
                .cat1(item.getCat1())
                .cat2(item.getCat2())
                .cat3(item.getCat3())
                .firstImage(item.getFirstimage())
                .firstImage2(item.getFirstimage2())
                .mapX(item.getMapx())
                .mapY(item.getMapy())
                .title(item.getTitle())
                .build();
    }

    public static PlaceResponse fromLocationBasedListItem(LocationBasedListItem item, ContentType contentType) {
        return PlaceResponse.builder()
                .addr((item.getAddr1() == null ? "" : item.getAddr1()) + " " + (item.getAddr2() == null ? "" : item.getAddr2()).trim())
                .areaCode(item.getAreacode())
                .sigunguCode(item.getSigungucode())
                .contentId(item.getContentid())
                .typeCode(String.valueOf(contentType.getContentTypeNo()))
                .cat1(item.getCat1())
                .cat2(item.getCat2())
                .cat3(item.getCat3())
                .firstImage(item.getFirstimage())
                .firstImage2(item.getFirstimage2())
                .mapX(item.getMapx())
                .mapY(item.getMapy())
                .title(item.getTitle())
                .build();
    }
}

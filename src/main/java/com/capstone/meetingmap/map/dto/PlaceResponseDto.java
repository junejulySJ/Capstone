package com.capstone.meetingmap.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlaceResponseDto {
    private String addr;
    private String areacode;
    private String sigungucode;
    private String contentid;
    private String createdtime;
    private String firstimage;
    private String firstimage2;
    private String mapx;
    private String mapy;
    private String mlevel;
    private String modifiedtime;
    private String tel;
    private String title;

    public static PlaceResponseDto fromAreaBasedListItem(AreaBasedListItem item) {
        return PlaceResponseDto.builder()
                .addr((item.getAddr1() == null ? "" : item.getAddr1()) + " " + (item.getAddr2() == null ? "" : item.getAddr2()).trim())
                .areacode(item.getAreacode())
                .sigungucode(item.getSigungucode())
                .contentid(item.getContentid())
                .createdtime(item.getCreatedtime())
                .firstimage(item.getFirstimage())
                .firstimage2(item.getFirstimage2())
                .mapx(item.getMapx())
                .mapy(item.getMapy())
                .mlevel(item.getMlevel())
                .modifiedtime(item.getModifiedtime())
                .tel(item.getTel())
                .title(item.getTitle())
                .build();
    }

    public static PlaceResponseDto fromKeywordBasedListItem(KeywordBasedListItem item) {
        return PlaceResponseDto.builder()
                .addr((item.getAddr1() == null ? "" : item.getAddr1()) + " " + (item.getAddr2() == null ? "" : item.getAddr2()).trim())
                .areacode(item.getAreacode())
                .sigungucode(item.getSigungucode())
                .contentid(item.getContentid())
                .createdtime(item.getCreatedtime())
                .firstimage(item.getFirstimage())
                .firstimage2(item.getFirstimage2())
                .mapx(item.getMapx())
                .mapy(item.getMapy())
                .mlevel(item.getMlevel())
                .modifiedtime(item.getModifiedtime())
                .tel(item.getTel())
                .title(item.getTitle())
                .build();
    }
}

package com.capstone.meetingmap.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//프론트에 areaBasedList를 전달할 dto
@Getter
@NoArgsConstructor
public class AreaBasedListResponseDto {
    private String addr;
    private String contentid;
    private String contenttypeid;
    private String createdtime;
    private String firstimage;
    private String firstimage2;
    private String mapx;
    private String mapy;
    private String mlevel;
    private String modifiedtime;
    private String tel;
    private String title;
    private String zipcode;

    @Builder
    public AreaBasedListResponseDto(String addr, String contentid, String contenttypeid, String createdtime, String firstimage, String firstimage2, String mapx, String mapy, String mlevel, String modifiedtime, String tel, String title, String zipcode) {
        this.addr = addr;
        this.contentid = contentid;
        this.contenttypeid = contenttypeid;
        this.createdtime = createdtime;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.mapx = mapx;
        this.mapy = mapy;
        this.mlevel = mlevel;
        this.modifiedtime = modifiedtime;
        this.tel = tel;
        this.title = title;
        this.zipcode = zipcode;
    }
}

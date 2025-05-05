package com.capstone.meetingmap.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailCommonResponseDto {
    private String contentid;
    private String contenttypeid;
    private String createdtime;
    private String homepage;
    private String modifiedtime;
    private String tel;
    private String telname;
    private String title;
    private String firstimage;
    private String firstimage2;
    private String addr;
    private String zipcode;
    private String overview;

    @Builder
    public DetailCommonResponseDto(String contentid, String contenttypeid, String createdtime, String homepage, String modifiedtime, String tel, String telname, String title, String firstimage, String firstimage2, String addr, String zipcode, String overview) {
        this.contentid = contentid;
        this.contenttypeid = contenttypeid;
        this.createdtime = createdtime;
        this.homepage = homepage;
        this.modifiedtime = modifiedtime;
        this.tel = tel;
        this.telname = telname;
        this.title = title;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.addr = addr;
        this.zipcode = zipcode;
        this.overview = overview;
    }
}

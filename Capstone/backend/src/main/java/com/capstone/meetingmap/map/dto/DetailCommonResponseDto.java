package com.capstone.meetingmap.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
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
}

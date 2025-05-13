package com.capstone.meetingmap.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SearchCategoryList {
    private String contentTypeId;
    private String cat1;
    private String cat2;
    private String cat3;
}

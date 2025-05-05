package com.capstone.meetingmap.map.mapper;

import java.util.List;

public class ContentTypeMapper {
    public static CatCode mapTypesToCat(List<String> googleTypes) {
        for (String type : googleTypes) {
            switch (type) {
                case "movie_theater":
                    return new CatCode("2", "A02", "A0202", "A02020200"); // 인문(문화/예술/역사) > 문화시설 > 영화관
                case "convenience_store":
                    return new CatCode("5", "A04", "A0401", "A04011000"); // 쇼핑 > 쇼핑 > 사후면세점(편의점)
                case "cafe":
                    return new CatCode("6", "A05", "A0502", "A050200900"); // 음식 > 음식점 > 카페/전통찻집
                // ...
            }
        }
        return new CatCode("7", "A06", "A0601", "A06010001"); // 기타
    }

    public record CatCode(String typeCode, String cat1, String cat2, String cat3) {
    }
}

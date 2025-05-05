package com.capstone.meetingmap;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tour.api")
@Getter
@Setter
public class TourApiProperties {
    private String baseUrl;
    private String key;
}

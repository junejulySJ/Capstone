package com.capstone.meetingmap;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.service.app")
@Getter
@Setter
public class KakaoApiProperties {
    private String baseUrl;
    private String mobilityBaseUrl;
    private String restApiKey;
}

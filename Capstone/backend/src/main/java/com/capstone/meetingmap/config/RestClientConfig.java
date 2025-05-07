package com.capstone.meetingmap.config;

import com.capstone.meetingmap.KakaoApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient kakaoMobilityRestClient(KakaoApiProperties kakaoApiProperties) {
        return RestClient.builder()
                .baseUrl(kakaoApiProperties.getMobilityBaseUrl())
                .defaultHeader("Authorization", "KakaoAK " + kakaoApiProperties.getRestApiKey())
                .build();
    }
}

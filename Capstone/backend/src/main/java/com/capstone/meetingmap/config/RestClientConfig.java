package com.capstone.meetingmap.config;

import com.capstone.meetingmap.api.kakao.properties.KakaoApiProperties;
import com.capstone.meetingmap.api.tmap.properties.TMapApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient kakaoRestClient(KakaoApiProperties kakaoApiProperties) {
        return RestClient.builder()
                .baseUrl(kakaoApiProperties.getBaseUrl())
                .defaultHeader("Authorization", "KakaoAK " + kakaoApiProperties.getRestApiKey())
                .build();
    }

    @Bean
    public RestClient tMapRestClient(TMapApiProperties tMapApiProperties) {
        return RestClient.builder()
                .baseUrl(tMapApiProperties.getBaseUrl())
                .defaultHeader("appKey", tMapApiProperties.getKey())
                .requestInterceptor((request, body, execution) -> {
                    System.out.println("Request URI: " + request.getURI());
                    System.out.println("Request Method: " + request.getMethod());
                    System.out.println("Request Headers: " + request.getHeaders());
                    return execution.execute(request, body);
                })
                .build();
    }
}

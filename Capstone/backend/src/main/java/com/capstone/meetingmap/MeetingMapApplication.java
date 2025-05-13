package com.capstone.meetingmap;

import com.capstone.meetingmap.api.kakao.properties.KakaoApiProperties;
import com.capstone.meetingmap.api.tmap.properties.TMapApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({GoogleApiProperties.class, TourApiProperties.class, KakaoApiProperties.class, TMapApiProperties.class})
public class MeetingMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingMapApplication.class, args);
	}

}

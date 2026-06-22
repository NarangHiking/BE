package com.naranghiking;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class NarangHikingApplication {

	@PostConstruct
	public void init() {
		// 자바측에서 생성되는 시각(LocalDateTime.now() 등)을 KST 로 고정.
		// DB 시각은 JDBC URL 의 connectionTimeZone/forceConnectionTimeZoneToSession 으로 처리됨.
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(NarangHikingApplication.class, args);
	}
}

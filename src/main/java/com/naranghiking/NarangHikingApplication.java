package com.naranghiking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NarangHikingApplication {

	public static void main(String[] args) {
		SpringApplication.run(NarangHikingApplication.class, args);
	}
}

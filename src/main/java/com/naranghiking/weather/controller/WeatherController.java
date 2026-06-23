package com.naranghiking.weather.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.weather.dto.WeatherResponse;
import com.naranghiking.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

        private final WeatherService weatherService;

        private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

        @GetMapping
        public ResponseEntity<ApiResult> getWeather(
                @RequestParam double lat,
                @RequestParam double lng) {
            List<WeatherResponse> result = weatherService.getWeather(lat, lng);
            return ResponseEntity.ok(ApiResult.success(result));
        }
}

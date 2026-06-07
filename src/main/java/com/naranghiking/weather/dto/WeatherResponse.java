package com.naranghiking.weather.dto;

public record WeatherResponse(
        String fcstDate,
        String fcstTime,
        String temperature,              // TMP 기온(℃)
        String sky,                      // SKY 1맑음 3구름많음 4흐림
        String precipitationType,        // PTY 0없음 1비 2비/눈 3눈 4소나기
        String precipitationProbability, // POP 강수확률(%)
        String humidity,                 // REH 습도(%)
        String windSpeed                 // WSD 풍속(m/s)
) {}
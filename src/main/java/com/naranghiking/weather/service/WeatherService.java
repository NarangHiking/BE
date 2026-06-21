package com.naranghiking.weather.service;

import com.naranghiking.weather.dto.KmaApiResponse;
import com.naranghiking.weather.dto.MtnGrid;
import com.naranghiking.weather.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestClient restClient;

    @Value("${data.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    private static final int[] BASE_TIMES = {2, 5, 8, 11, 14, 17, 20, 23};

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public List<WeatherResponse> getWeather(String mtnName, String fcstDate) {
        MtnGrid grid = MtnGrid.fromName(mtnName);
        String [] base = resolveBaseDateTime();

        URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("serviceKey", apiKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1000)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", base[0])
                .queryParam("base_time", base[1])
                .queryParam("nx", grid.getNx())
                .queryParam("ny", grid.getNy())
                .build(true)
                .toUri();

        KmaApiResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(KmaApiResponse.class);

        if (response == null || !"00".equals(response.response().header().resultCode())) {
            throw new IllegalStateException("기상청 API 호출 실패: " + ((response != null) ? response.response().header().resultMsg() : "no response"));
        }

        List<KmaApiResponse.Item> items = response.response().body().items().item();

        Map<String, List<KmaApiResponse.Item>> byTime = items.stream()
                .filter(i -> i.fcstDate().equals(fcstDate))
                .collect(Collectors.groupingBy(
                        KmaApiResponse.Item::fcstTime, TreeMap::new, Collectors.toList()));
        return byTime.entrySet().stream()
                .map(e -> toWeatherResponse(fcstDate, e.getKey(), e.getValue()))
                .toList();
    }

    private WeatherResponse toWeatherResponse(String date, String time,
                                              List<KmaApiResponse.Item> items) {
        Map<String, String> v = items.stream()
                .collect(Collectors.toMap(
                        KmaApiResponse.Item::category,
                        KmaApiResponse.Item::fcstValue,
                        (a, b) -> a));
        return new WeatherResponse(
                date, time,
                v.get("TMP"), v.get("SKY"), v.get("PTY"),
                v.get("POP"), v.get("REH"), v.get("WSD"));
    }

    private String[] resolveBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();

        int baseTime = -1;
        for (int i = BASE_TIMES.length - 1; i>=0; i--) {
            int t = BASE_TIMES[i];
            if (hour > t || (hour == t && minute >= 10)) {
                baseTime = t;
                break;
            }
        }

        if (baseTime == -1) {
            now = now.minusDays(1);
            baseTime = 23;
        }

        String baseDate = now.format(DATE_FMT);
        String baseTimeStr = String.format("%02d00", baseTime);
        return new String [] {baseDate, baseTimeStr};
    }
}

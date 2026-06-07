package com.naranghiking.suntimes.service;

import com.naranghiking.suntimes.dto.SuntimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SuntimeService {

    private final RestClient restClient;

    @Value("${data.api.key}")
    private String serviceKey;

    public SuntimeResponse getSuntimes(String locdate, String location) throws UnsupportedEncodingException {
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);

        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getAreaRiseSetInfo")
                .queryParam("serviceKey", serviceKey)
                .queryParam("locdate", locdate)
                .queryParam("location", encodedLocation)
                .queryParam("_type", "json")
                .build(true)
                .toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(SuntimeResponse.class);
    }

    public URI getUri(String locdate, String location) {
        return UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getAreaRiseSetInfo")
                .queryParam("serviceKey", serviceKey)
                .queryParam("locdate", locdate)
                .queryParam("location", URLEncoder.encode("location", StandardCharsets.UTF_8))
                .queryParam("_type", "json")
                .build(true)
                .toUri();
    }
}

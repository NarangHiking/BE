package com.naranghiking.suntimes.service;

import com.naranghiking.suntimes.dto.SuntimeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SuntimeServiceTest {



    @Autowired
    private SuntimeService suntimeService;

    @Autowired
    private RestClient restClient;

    @Test
    void getSunInfo() throws UnsupportedEncodingException {
        SuntimeResponse response = suntimeService.getSuntimes("20260606", "구미");
        System.out.println(response);
    }
}
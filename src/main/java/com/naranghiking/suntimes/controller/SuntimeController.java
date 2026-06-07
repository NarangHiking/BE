package com.naranghiking.suntimes.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.suntimes.dto.SuntimeResponse;
import com.naranghiking.suntimes.service.SuntimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sun")
public class SuntimeController {

    private final SuntimeService suntimeService;

    @GetMapping
    public ResponseEntity<ApiResult> getSunInfo(
            @RequestParam String locdate,
            @RequestParam String location) {
        try {
            SuntimeResponse response = suntimeService.getSuntimes(locdate, location);
            return ResponseEntity.ok(ApiResult.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.fail("유효하지 않은 입력값입니다."));
        }
    }
}

package com.naranghiking.recommend.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.recommend.service.RecommendService;
import com.naranghiking.track.dto.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/{trackId}")
    public ResponseEntity<ApiResult> insert(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long trackId
    ) {
        try {
            recommendService.insert(userId, trackId);
            return ResponseEntity.ok(ApiResult.success("ok"));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResult.fail("이미 추천한 경로입니다."));
        }
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<ApiResult> delete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long trackId
    ) {
        int result = recommendService.delete(userId, trackId);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.fail("추천한 경로가 없습니다."));
        }
        return ResponseEntity.ok(ApiResult.success("ok"));
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<ApiResult> isExist(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long trackId
    ) {
        return ResponseEntity.ok(ApiResult.success(recommendService.isExist(userId, trackId)));
    }

    @GetMapping("/track")
    public ResponseEntity<ApiResult> selectByUserId(
            @AuthenticationPrincipal Long userId
    ) {
        List<Track> tracks = recommendService.selectRecommends(userId);
        return ResponseEntity.ok(ApiResult.success(tracks));
    }
}

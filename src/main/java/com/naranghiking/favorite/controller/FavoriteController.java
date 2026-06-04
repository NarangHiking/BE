package com.naranghiking.favorite.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.favorite.service.FavoriteService;
import com.naranghiking.track.dto.Track;
import com.naranghiking.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{trackId}")
    public ResponseEntity<ApiResult> insert(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long trackId
    ) {
        try {
            System.out.println("userId: " + userId + ", trackId: " + trackId);
            favoriteService.insert(userId, trackId);
            return ResponseEntity.ok(ApiResult.success("ok"));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResult.fail("이미 즐겨찾기한 경로입니다."));
        }
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<ApiResult> delete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long trackId
    ) {
        int result = favoriteService.delete(userId, trackId);
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
        return ResponseEntity.ok(ApiResult.success(favoriteService.isExist(userId, trackId)));
    }

    @GetMapping("/track")
    public ResponseEntity<ApiResult> selectByUserId(
            @AuthenticationPrincipal Long userId
    ) {
        List<Track> tracks = favoriteService.selectFavorites(userId);
        return ResponseEntity.ok(ApiResult.success(tracks));
    }
}

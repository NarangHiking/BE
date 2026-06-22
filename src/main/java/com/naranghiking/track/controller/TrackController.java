package com.naranghiking.track.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.common.service.R2Service;
import com.naranghiking.track.dto.Track;
import com.naranghiking.track.dto.TrackCondition;
import com.naranghiking.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/track")
public class TrackController {
    private final TrackService trackService;
    private final R2Service r2Service;

    @GetMapping("/search")
    public ResponseEntity<ApiResult> selectByName(@RequestParam String name) {
        List<Track> li = trackService.selectByName(name);
        return ResponseEntity.ok(ApiResult.success(li));
    }

    @GetMapping
    public ResponseEntity<ApiResult> selectByCondition(@ModelAttribute TrackCondition condition) {
        List<Track> li = trackService.selectByCondition(condition);
        return ResponseEntity.ok(ApiResult.success(li));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult> selectById(@PathVariable Long id) {
        Track track = trackService.selectById(id);
        if (track == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND) // UNAUTHORIZED → NOT_FOUND
                    .body(ApiResult.fail("해당 아이디의 경로를 찾을 수 없습니다."));
        return ResponseEntity.ok(ApiResult.success(track));
    }

    @PostMapping(value = "/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult> bulkInsert(
            @RequestPart("tracks") List<Track> tracks,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        // files[i] ↔ tracks[i] 순서로 매칭. 하나라도 실패하면 전부 롤백(R2 보상 삭제 포함)
        List<Track> result = trackService.bulkInsert(tracks, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResult> insert(
            @RequestPart Track t,
            @RequestPart(value="file", required = false) MultipartFile file
            ) throws IOException {
        // id 중복 체크 제거 (AUTO_INCREMENT라 의미 없음)

        if (file != null) {
            Long mtnId = t.getMountainId();
            String storedFilename = r2Service.uploadFile(file, "gpx/" + mtnId);
            t.setGpxFilePath(storedFilename);
        }

        trackService.insert(t);
        t.setGpxUrl(r2Service.getPublicUrl(t.getGpxFilePath()));
        return ResponseEntity.ok(ApiResult.success(t)); // ApiResult.success 중첩 제거
    }

    @PutMapping
    public ResponseEntity<ApiResult> update(@RequestPart Track t, @RequestPart(value="file", required = false) MultipartFile file) throws IOException {
        Track target = trackService.selectById(t.getId());
        if (target == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.fail("해당 경로가 존재하지 않습니다."));

        if (file != null) {
            r2Service.deleteFile(target.getGpxFilePath());
            Long mtnId = t.getMountainId();
            String storedName = r2Service.uploadFile(file, "gpx/" + mtnId);
            t.setGpxFilePath(storedName);
        }
        trackService.update(t);
        t.setGpxUrl(r2Service.getPublicUrl(t.getGpxFilePath()));
        return ResponseEntity.ok(ApiResult.success(t));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult> delete(@PathVariable Long id) {
        Track target = trackService.selectById(id);
        if (target == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResult.fail("해당 경로가 존재하지 않습니다."));
        try {
            r2Service.deleteFile(target.getGpxFilePath() );
        } catch (Exception e) {
            log.warn("gpx 파일이 존재하지 않습니다.");
        } finally {
            trackService.delete(id);
        }
        return ResponseEntity.ok(ApiResult.success("ok"));
    }
}
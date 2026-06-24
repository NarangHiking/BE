package com.naranghiking.mtn.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.common.service.R2Service;
import com.naranghiking.mtn.dto.Mtn;
import com.naranghiking.mtn.service.MtnService;
import com.naranghiking.track.dto.Track;
import com.naranghiking.track.service.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/mtn")
@RequiredArgsConstructor
public class MtnController {

    private final MtnService mtnService;
    private final TrackService trackService;
    private final R2Service r2Service;

    @Operation(summary = "산 정보 전체 조회", description = "등록된 산 정보를 모두 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="조회 성공"),
    })
    @GetMapping("/list")
    public ResponseEntity<ApiResult> selectAll() {
        List<Mtn> mtns = mtnService.selectAll();
        return ResponseEntity.ok(ApiResult.success(mtns));
    }

    @Operation(summary = "추천순 산 목록 조회", description = "코스 추천(하트) 수 기준 내림차순 정렬. limit 파라미터로 개수 제한 (미지정 시 전체)")
    @GetMapping("/top")
    public ResponseEntity<ApiResult> selectByRecommend(
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(ApiResult.success(mtnService.selectByRecommend(limit)));
    }

    @Operation(summary = "산 정보 개별 조회", description = "해당 산 ID의 정보를 불러옵니다..")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="조회 성공"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult> select(
            @PathVariable Long id
    ) {
        Mtn mtn = mtnService.select(id);
        if (mtn == null) {
            throw new RuntimeException("해당 번호의 산이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(ApiResult.success(mtn));
    }

    @Operation(summary = "산 경로 조회", description = "산에 포함된 경로 정보를 모두 불러옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="조회 성공"),
    })
    @GetMapping("/{mtnId}/track")
    public ResponseEntity<ApiResult> selectByMtnId(@PathVariable Long mtnId) {
        List<Track> li = trackService.selectByMtnId(mtnId);
        return ResponseEntity.ok(ApiResult.success(li));
    }

    @Operation(summary = "산 정보 삽입", description = "산을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="등록 성공"),
    })
    @PostMapping
    public ResponseEntity<ApiResult> insert(
            @RequestPart Mtn mtn,
            @RequestPart(value="file", required = false) MultipartFile file
            ) throws IOException {
        Mtn target = mtnService.select(mtn.getId());
        if (target != null) {
            throw new RuntimeException("해당 산이 이미 존재합니다.");
        }

        String storedFilename = null;
        if (file != null && !file.isEmpty()) {
            mtn.setOriginalFilename(file.getOriginalFilename());
            storedFilename = r2Service.uploadFile(file, "images/mtn");
            mtn.setStoredFilename(storedFilename);
        }

        try {
            mtnService.insert(mtn);
        } catch (Exception e) {
            // DB 저장 실패 시 R2에 올린 파일을 정리(고아 파일 방지)한 뒤 예외를 다시 던진다.
            if (storedFilename != null) {
                r2Service.deleteFile(storedFilename);
            }
            throw e;
        }

        mtn.setImageUrl(r2Service.getPublicUrl(mtn.getStoredFilename()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(mtn));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult> update(
            @PathVariable Long id,
            @RequestPart Mtn mtn,
            @RequestPart(value="file", required = false) MultipartFile file
    ) throws IOException {
        Mtn target = mtnService.select(id);
        if (target == null) {
            throw new RuntimeException("해당 산이 존재하지 않습니다.");
        }

        if (file != null && !file.isEmpty()) {
            // 기존 이미지가 있을 때만 삭제 (없으면 null 키로 R2 호출돼 "Parameter 'Key' must not be null" 발생)
            if (target.getStoredFilename() != null && !target.getStoredFilename().isBlank()) {
                r2Service.deleteFile(target.getStoredFilename());
            }
            mtn.setOriginalFilename(file.getOriginalFilename());
            mtn.setStoredFilename(r2Service.uploadFile(file, "images/mtn"));
        } else {
            // 새 파일이 없으면 기존 파일명을 유지 (update 시 null 로 덮어쓰지 않도록)
            mtn.setOriginalFilename(target.getOriginalFilename());
            mtn.setStoredFilename(target.getStoredFilename());
        }


        mtnService.update(mtn);
        mtn.setImageUrl(r2Service.getPublicUrl(mtn.getStoredFilename()));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.success(mtn));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult> delete(
            @PathVariable Long id
    ) {
        Mtn target = mtnService.select(id);
        String storedFilename = target.getStoredFilename();
        if (storedFilename != null && !storedFilename.isBlank())
            r2Service.deleteFile(target.getStoredFilename());

        mtnService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.success("ok"));
    }
}

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

        String originalFilename = file.getOriginalFilename();
        String storedFilename = r2Service.uploadFile(file, "images/mountain");

        mtn.setOriginalFilename(originalFilename);
        mtn.setStoredFilename(storedFilename);

        mtnService.insert(mtn);
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

        if (file != null) {
            r2Service.deleteFile(target.getStoredFilename());
            String originalFilename = file.getOriginalFilename();
            String storedFilename = r2Service.uploadFile(file, "images/mountain");
            mtn.setOriginalFilename(originalFilename);
            mtn.setStoredFilename(storedFilename);
        }


        mtnService.update(mtn);
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

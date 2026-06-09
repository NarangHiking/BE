package com.naranghiking.trackComment.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.dto.TrackCommentRequest;
import com.naranghiking.trackComment.service.TrackCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "각 등산 코스의 후기와 관련된 컨트롤러")
@RestController
@RequestMapping("/track/{trackId}/comment")
@RequiredArgsConstructor
public class TrackCommentController {

    private final TrackCommentService trackCommentService;

    @Operation(summary = "코스 전체 후기 조회", description = "전체 후기를 조회하는 요청 처리")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping // 후기 전체 조회
    public ResponseEntity<ApiResult<List<TrackCommentListResponse>>> list(
            @Parameter(description = "코스의 ID", example = "1L")
            @PathVariable("trackId") Long trackId) {
        List<TrackCommentListResponse> result = trackCommentService.selectAll(trackId);
        return ResponseEntity.ok(ApiResult.success(result));
    }


    @Operation(summary = "코스 후기 작성", description = "코스 후기를 작성하는 요청 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "후기 작성 성공"),
            @ApiResponse(responseCode = "400", description = "후기 작성 실패, 필수 정보 누락"),
            @ApiResponse(responseCode = "500", description = "후기 작성 실패, 서버 문제")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 후기 작성
    public ResponseEntity<ApiResult<String>> create(
            @Parameter(description = "코스의 ID", example = "1L")
            @PathVariable("trackId") Long trackId,

            @Parameter(description = "작성자의 ID", example = "1L")
            @AuthenticationPrincipal Long userId,

            @Parameter(description = "후기의 내용", example = "TrackCommentRequest 참고")
            @Valid @RequestPart("comment") TrackCommentRequest comment,

            @Parameter(description = "후기에 첨부된 이미지", example = "이미지1, 이미지2")
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        comment.setUserId(userId);
        comment.setTrackId(trackId);
        trackCommentService.insert(comment, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success("저장 완료"));
    }


    @Operation(summary = "코스 후기 수정", description = "코스 후기를 수정하는 요청 처리")
    @PutMapping
    public ResponseEntity<ApiResult<Void>> update() {

        return ResponseEntity.ok(null);
    }


    @Operation(summary = "코스 후기 삭제", description = "코스 후기를 논리적으로 삭제하는 요청 처리")
    @DeleteMapping
    public ResponseEntity<ApiResult<Void>> delete() {

        return ResponseEntity.ok(null);
    }
}

package com.naranghiking.trackComment.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.service.TrackCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "각 등산 코스의 후기와 관련된 컨트롤러")
@RestController
@RequestMapping("/track/{trackId}/comment")
@RequiredArgsConstructor
public class TrackCommentController {

    private final TrackCommentService trackCommentService;

    // 후기 전체 조회, 후기 작성(사진은 1장만?), 후기 수정, 후기 삭제
    // 트랙에 대한 후기 전체, 후기 단건 조회, 후기 작성, 후기 수정, 후기 삭제만 해보자
    @Operation(summary = "코스 전체 후기 조회", description = "전체 후기를 조회하는 요청 처리")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<ApiResult<List<TrackCommentListResponse>>> list(
            @Parameter(description = "코스의 ID", example = "1L")
            @PathVariable("trackId") Long trackId) {
        List<TrackCommentListResponse> result = trackCommentService.selectAll(trackId);
        return ResponseEntity.ok(ApiResult.success(result));
    }
}

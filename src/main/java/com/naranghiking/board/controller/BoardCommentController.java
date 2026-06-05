package com.naranghiking.board.controller;

import com.naranghiking.board.dto.BoardCommentRequest;
import com.naranghiking.board.dto.BoardCommentResponse;
import com.naranghiking.board.service.BoardCommentService;
import com.naranghiking.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글 댓글과 관련된 컨트롤러")
@RestController
@RequestMapping("/board/{boardId}/comment") // boardId의 게시글 안에서만 일어나는 요청들
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Operation(summary = "게시글의 댓글 작성", description = "boardId의 게시글에 댓글 작성 요청 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 작성 실패, 필수 정보 누락"),
            @ApiResponse(responseCode = "500", description = "댓글 생성 실패, 서버 문제")
    })
    @PostMapping
    public ResponseEntity<ApiResult<BoardCommentResponse>> create(
            @Parameter(description = "사용자의 id, redis에서 추출", example = "1")
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "댓글을 작성할 게시글의 ID", example = "2")
            @PathVariable("boardId") Long boardId,
            @Parameter(description = "댓글 작성에 필요한 내용", example = "BoardCommentRequest 참고")
            @Valid @RequestBody BoardCommentRequest comment) {
        comment.setUserId(userId);
        BoardCommentResponse result = boardCommentService.insert(boardId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(result));
    }



    @Operation(summary = "게시글의 댓글 수정", description = "boardId 게시글의 댓글을 수정하는 요청 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 수정 실패, 필수 정보 누락"),
            @ApiResponse(responseCode = "403", description = "댓글 수정 실패, 권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글 수정 실패, 해당 댓글이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "댓글 수정 실패, 서버 문제")
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResult<BoardCommentResponse>> update(
            @Parameter(description = "사용자의 id, redis에서 추출", example = "1")
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "수정할 댓글의 ID", example = "1")
            @PathVariable("commentId") Long commentId,
            @Parameter(description = "댓글 수정에 필요한 내용", example = "BoardCommentRequest 참고")
            @Valid @RequestBody BoardCommentRequest comment) {
        comment.setId(commentId);
        comment.setUserId(userId);
        BoardCommentResponse result = boardCommentService.update(comment);
        return ResponseEntity.ok(ApiResult.success(result));
    }


    @Operation(summary = "게시글의 댓글 삭제 요청 처리", description = "해당 게시글의 댓글을 삭제, 물리적이 아닌 논리적 삭제 적용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "댓글 삭제 실패, 권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글 삭제 실패, 해당 댓글이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "댓글 삭제 실패, 서버 문제")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResult<String>> delete(
            @Parameter(description = "사용자의 id, redis에서 추출", example = "1")
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "삭제할 댓글의 ID", example = "1")
            @PathVariable("commentId") Long commentId) {
        boardCommentService.delete(commentId, userId);
        return ResponseEntity.ok(ApiResult.success("성공적으로 삭제되었습니다."));
    }
}

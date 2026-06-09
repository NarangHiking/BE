package com.naranghiking.board.controller;

import com.naranghiking.board.dto.BoardDetailResponse;
import com.naranghiking.board.dto.BoardListResponse;
import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.board.service.BoardService;
import com.naranghiking.common.dto.ApiResult;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Tag(name = "게시글과 관련된 컨트롤러")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 전체 조회", description = "전체 게시글을 조회합니다. 때로는 각각의 파라미터에 맞춰, 제목, 내용, 카테고리로도 조회가 가능합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping // 게시글 전체(키워드, 카테고리) 조회
    public ResponseEntity<ApiResult<List<BoardListResponse>>> list(
            @Parameter(description = "제목 및 내용 검색", example = "오늘은") @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "카테고리로 검색", example = "자유") @RequestParam(value = "category", required = false) String category) {
        List<BoardListResponse> boards = boardService.selectAll(keyword, category); // 키워드랑 카테고리 같이 전달
        // boards가 비어있으면 null이 아닌 비어있는 리스트를 반환
        return ResponseEntity.ok(ApiResult.success(Objects.requireNonNullElse(boards, Collections.emptyList())));
    }



    @Operation(summary = "게시글 상세 조회", description = "boards의 PK인 id를 통해서 단건의 게시글만 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회 실패, 게시글을 찾을 수 없습니다.")
    })
    @GetMapping("/{id}") // 게시글 상세 조회
    public ResponseEntity<ApiResult<BoardDetailResponse>> detail(
            @Parameter(description = "게시글 ID, 단일 게시글 조회에 필요", example = "1")
            @PathVariable("id") Long id) {
        BoardDetailResponse board = boardService.selectById(id);
        return ResponseEntity.ok(ApiResult.success(board));
    }



    @Operation(summary = "게시글 작성", description = "form에 맞게 작성된 게시글을 DB에 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 생성 실패, 필수 정보 누락"),
            @ApiResponse(responseCode = "500", description = "게시글 생성 실패, 서버 문제")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 게시글 작성
    public ResponseEntity<ApiResult<BoardDetailResponse>> create(
            @Parameter(description = "사용자의 id, redis에서 추출", example = "1")
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "게시글 내용", example = "BoardRequest 참고")
            @Valid @RequestPart("board") BoardRequest board,
            @Parameter(description = "게시글에 첨부된 이미지", example = "이미지1, 이미지2")
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        board.setUserId(userId);
        boardService.insert(board, images);
        // 새롭게 생성된 게시글 전달
        BoardDetailResponse result = boardService.selectById(board.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(result));
    }



    @Operation(summary = "게시글 수정", description = "id로 게시글을 특정해서 board로 전달된 내용으로 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 수정 실패, 필수 정보 누락 등"),
            @ApiResponse(responseCode = "403", description = "게시글 수정 실패, 권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글 수정 실패, 해당 id로 게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "게시글 수정 실패, 서버 문제")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 게시글 수정
    public ResponseEntity<ApiResult<BoardDetailResponse>> update(
            @Parameter(description = "사용자의 id, redis에서 추출", example = "1")
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "게시글의 ID", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "게시글 내용", example = "BoardRequest 참고")
            @Valid @RequestPart("board") BoardRequest board,
            @Parameter(description = "새롭게 추가될 이미지들", example = "이미지1, 이미지2")
            @RequestPart(value = "addedImages", required = false) List<MultipartFile> addedImages,
            @Parameter(description = "삭제될 이미지들", example = "이미지1, 이미지2")
            @RequestParam(value = "deletedImages", required = false) List<String> deletedImages) {
        // 업데이트 진행
        board.setUserId(userId);
        boardService.update(id, board, addedImages, deletedImages);
        // 새롭게 업데이트된 게시물 조회해서 반환
        BoardDetailResponse updated = boardService.selectById(id);
        return ResponseEntity.ok(ApiResult.success(updated));
    }



    @Operation(summary = "게시글 삭제", description = "id로 게시글을 특정해서 삭제 로직을 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "게시글 삭제 실패, 권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글 삭제 실패, 해당 id로 게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "게시글 삭제 실패, 서버 문제")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> delete(
            @Parameter(description = "사용자의 id, redis에서 추출", example = "1")
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "게시글의 ID", example = "1")
            @PathVariable("id") Long id) {
        boardService.deleteById(id, userId);
        return ResponseEntity.ok(ApiResult.success("성공적으로 삭제하였습니다."));
    }
}

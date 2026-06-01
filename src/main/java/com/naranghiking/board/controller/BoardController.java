package com.naranghiking.board.controller;

import com.naranghiking.board.dto.BoardDetailResponse;
import com.naranghiking.board.dto.BoardListResponse;
import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.board.dto.BoardResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "게시글과 관련된 컨트롤러", description = "게시글 전체 조회, 게시글 상세 조회")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 전체 조회", description = "전체 게시글을 조회합니다. 때로는 각각의 파라미터에 맞춰, 제목, 내용, 카테고리로도 조회가 가능합니다.")
    @ApiResponse(responseCode = "200",description = "조회 성공")
    @GetMapping // 게시글 전체(키워드, 카테고리) 조회
    public ResponseEntity<ApiResult<List<BoardListResponse>>> list(
            @Parameter(description = "제목 및 내용 검색", example = "오늘은") @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "카테고리로 검색", example = "자유") @RequestParam(value = "category", required = false) String category) {
        List<BoardListResponse> boards = boardService.selectAll(keyword, category); // 키워드랑 카테고리 같이 전달
        // 게시글이 비어있으면 null이 아닌 비어있는 리스트를 반환
        if(boards == null) return ResponseEntity.ok(ApiResult.success(Collections.emptyList()));
        return ResponseEntity.ok(ApiResult.success(boards));
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
    @PostMapping // 게시글 작성
    public ResponseEntity<ApiResult<BoardRequest>> create(@Valid @RequestBody BoardRequest board) {
        boardService.insert(board);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(board));
    }

    @Operation(summary = "게시글 수정", description = "id로 게시글을 특정해서 board로 전달된 내용으로 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 수정 실패, 필수 정보 누락 등"),
            @ApiResponse(responseCode = "404", description = "게시글 수정 실패, 해당 id로 게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "게시글 수정 실패, 서버 문제")
    })
    @PutMapping("/{id}") // 게시글 수정
    public ResponseEntity<ApiResult<BoardResponse>> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody BoardRequest board) {
        BoardResponse updated = boardService.update(id, board);
        return ResponseEntity.ok(ApiResult.success(updated));
    }

    @Operation(summary = "게시글 삭제", description = "id로 게시글을 특정해서 삭제 로직을 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 삭제 실패, 해당 id로 게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "게시글 삭제 실패, 서버 문제")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> delete(@PathVariable("id") Long id) {
        boardService.deleteById(id);
        return ResponseEntity.ok(ApiResult.success("성공적으로 삭제하였습니다."));
    }
}

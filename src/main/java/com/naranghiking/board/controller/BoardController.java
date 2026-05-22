package com.naranghiking.board.controller;

import com.naranghiking.board.dto.BoardResponse;
import com.naranghiking.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "게시글과 관련된 컨트롤러", description = "게시글 전체 조회, 게시글 단건 조회")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 전체 조회", description = "전체 게시글을 조회합니다.")
    @ApiResponse(responseCode = "200",description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<BoardResponse>> selectAll() { // 테스트 확인 완료
        List<BoardResponse> boards = boardService.selectAll();

        return ResponseEntity.ok(boards);
    }

    @Operation(summary = "게시글 단건 조회", description = "boards의 PK인 id를 통해서 단건의 게시글만 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> selectById(
            @Parameter(description = "게시글 ID, 단일 게시글 조회에 필요", example = "1")
            @PathVariable long id) {
        BoardResponse board = boardService.selectById(id);
        // 게시글을 찾지 못하면 404 반환
        if(board == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(board);
    }
}

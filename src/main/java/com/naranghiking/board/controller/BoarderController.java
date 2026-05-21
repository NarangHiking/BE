package com.naranghiking.board.controller;

import com.naranghiking.board.dto.BoardResponse;
import com.naranghiking.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "게시글과 관련된 컨트롤러", description = "테스트 코드만 존재")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoarderController {

    private final BoardService boardService;

    @Operation(summary = "게시글 전체 조회 테스트", description = "service단에서 더미 데이터 생성해서 요청 시 두 건만 리턴")
    @ApiResponse(responseCode = "200",description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<BoardResponse>> test() { // 테스트 확인 완료
        List<BoardResponse> result = boardService.selectAll();

        return ResponseEntity.ok(result);
    }
}

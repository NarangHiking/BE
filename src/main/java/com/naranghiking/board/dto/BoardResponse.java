package com.naranghiking.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "게시글 조회에 필요한 DTO")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    @Schema(description = "사용자 ID, 반드시 필요", example = "1")
    private long userId;

    @Schema(description = "등산 코스 ID, 건의사항일 때만 선택하도록 한다.", example = "1")
    private long trackId;

    @Schema(description = "게시글의 제목", example = "오늘의 등산 코스")
    private String title;

    @Schema(description = "게시글의 내용", example = "즐겁게 등산 후 복귀 중~ 이 코스 좋네요~")
    private String content;

    @Schema(description = "게시글의 카테고리, 자유 또는 건의사항", example = "free, feedback")
    private String category;

    @Schema(description = "게시글 생성일", example = "2025-05-21")
    private LocalDateTime createdAt;
}

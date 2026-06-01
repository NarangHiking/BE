package com.naranghiking.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게시글의 댓글 조회를 위한 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardCommentResponse {
    @Schema(description = "댓글의 ID, 해당 댓글을 선택할 때 필요", example = "1")
    private Long id;

    @Schema(description = "댓글의 작성자", example = "김수한무")
    private String name;

    @Schema(description = "댓글의 내용", example = "댓글 예시입니다.")
    private String content;

    @Schema(description = "댓글 생성일, 댓글 작성일 표시에 필요", example = "2026-06-01T22:08:26")
    private String createdAt;
}

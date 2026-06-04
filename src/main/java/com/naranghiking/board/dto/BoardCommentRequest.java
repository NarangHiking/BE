package com.naranghiking.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "게시글 댓글을 작성하기 위한 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardCommentRequest {
    @Schema(description = "댓글의 ID", example = "1")
    private Long id;

    @Schema(description = "작성자의 ID", example = "2")
    @NotNull
    private Long userId;

    @Schema(description = "댓글의 내용", example = "좋은 정보 감사합니다.")
    @NotBlank
    private String content;
}

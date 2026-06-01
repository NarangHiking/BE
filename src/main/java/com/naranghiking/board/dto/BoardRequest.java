package com.naranghiking.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "게시글 작성에 필요한 DTO")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {
    @Schema(description = "게시글의 ID, 게시글 작성 시 자동으로 생성", example = "1")
    private Long id;

    @Schema(description = "사용자 ID, 반드시 필요", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "등산 코스 ID, 건의사항일 때만 선택하도록 한다.", example = "1")
    private Long trackId;

    @Schema(description = "게시글의 제목", example = "오늘의 등산 코스")
    @NotBlank
    private String title;

    @Schema(description = "게시글의 내용", example = "즐겁게 등산 후 복귀 중~ 이 코스 좋네요~")
    @NotBlank
    private String content;

    @Schema(description = "게시글의 카테고리, 자유 또는 건의사항", example = "free, feedback")
    @NotBlank
    private String category;
}

package com.naranghiking.board.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "게시글 전체 조회를 위한 DTO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardListResponse {
    @Schema(description = "게시글의 ID, 해당 게시글을 선택할 때 필요", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "게시글 작성자, 해당 게시글의 작성자 표시", example = "김수한무")
    @NotNull
    private String name;

    @Schema(description = "게시글의 제목", example = "오늘의 등산 코스")
    @NotBlank
    private String title;

    @Schema(description = "게시글의 댓글 수", example = "1")
    private int commentCount;

    @Schema(description = "게시글의 카테고리, 자유 또는 건의사항", example = "free or feedback")
    @NotBlank
    private String category;

    @Schema(description = "게시글의 이미지, 대표 이미지로 한 장만 표시", example = "/images/")
    private String image;

    @Schema(description = "게시글 생성일", example = "2025-05-21")
    private LocalDateTime createdAt;

    public String getImage() { // 대표 이미지가 없을 경우에는 default_image를 전달
        if(this.image == null || this.image.isEmpty()) {
            return "default_image.png";
        }
        return this.image;
    }
}

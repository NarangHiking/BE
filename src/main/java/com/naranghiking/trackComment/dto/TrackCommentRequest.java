package com.naranghiking.trackComment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "코스의 후기 작성을 위한 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackCommentRequest {
    @Schema(description = "후기의 ID, 게시글 생성 시 자동으로 주입", example = "1L")
    private Long id;

    @Schema(description = "후기 작성자의 ID", example = "1L")
    private Long userId;

    @Schema(description = "후기를 작성할 코스의 ID", example = "1L")
    private Long trackId;

    @Schema(description = "후기 내용", example = "도중에 경치가 이쁜 곳이 있어요!")
    @NotBlank
    private String content;
}

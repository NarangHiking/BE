package com.naranghiking.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이미지 조회를 위한 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    @Schema(description = "원본 이미지", example = "원본.jpg")
    private String originalFilename;

    @Schema(description = "저장될 이미지", example = "random.jpg")
    private String storedFilename;
}

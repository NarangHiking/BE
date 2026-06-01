package com.naranghiking.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "이미지 저장을 위한 DTO")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {
    @Schema(description = "원본 이미지", example = "원본.jpg")
    @NotBlank
    private String originalFilename;

    @Schema(description = "저장될 이미지", example = "random.jpg")
    @NotBlank
    private String storedFilename;
}

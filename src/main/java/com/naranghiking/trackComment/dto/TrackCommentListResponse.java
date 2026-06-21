package com.naranghiking.trackComment.dto;

import com.naranghiking.common.dto.ImageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "코스의 전체 후기 조회를 위한 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackCommentListResponse {
    @Schema(description = "후기의 ID, 해당 후기를 선택할 때 필요", example = "1L")
    private Long id;

    @Schema(description = "후기 작성자의 ID, 유효성 검사를 위해 필요", example = "1L")
    private Long userId;

    @Schema(description = "후기 작성자의 이름", example = "홍길동")
    private String name;

    @Schema(description = "후기의 내용", example = "이 코스 정말 좋네요")
    private String content;

    @Schema(description = "후기 작성 일자", example = "2026년06월01일, 22시36분")
    private String createdAt;

    @Schema(description = "후기에 선택된 이미지들의 원본과 저장된 이름들(R2 키)", example = "{원본1, 저장1}, {원본2, 저장2}")
    private List<ImageResponse> images;

    @Schema(description = "후기 이미지들의 화면 표시용 공개 URL 리스트 (images의 storedFilename으로부터 생성)", example = "https://pub-xxxx.r2.dev/trackComment/uuid1.jpg")
    private List<String> imageUrls;
}

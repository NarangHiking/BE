package com.naranghiking.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Schema(description = "게시글 상세 조회에 필요한 DTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailResponse {
    @Schema(description = "게시글의 ID, 해당 게시글을 선택할 때 필요", example = "1L")
    private Long id;

    @Schema(description = "게시글 작성자의 ID, 유효성 검사를 위해", example = "2L")
    private Long userId;

    @Schema(description = "게시글 작성자, 해당 게시글의 작성자 표시", example = "김수한무")
    private String name;

    @Schema(description = "등산 코스 ID, 건의사항일 때만 선택하도록 한다.", example = "1L")
    private Long trackId;

    @Schema(description = "게시글의 제목", example = "오늘의 등산 코스")
    private String title;

    @Schema(description = "게시글의 내용", example = "즐겁게 등산 후 복귀 중~ 이 코스 좋네요~")
    private String content;

    @Schema(description = "게시글의 카테고리, 자유 또는 건의사항", example = "free, feedback")
    private String category;

    @Schema(description = "게시글에 달린 댓글 수", example = "1")
    private int commentCount;

    @Schema(description = "게시글의 이미지들 (R2 저장 키), 리스트 형태로 전달", example = "board/uuid1.jpg, board/uuid2.jpg")
    private List<String> images;

    @Schema(description = "게시글 이미지들의 화면 표시용 공개 URL 리스트 (images 키로부터 생성)", example = "https://pub-xxxx.r2.dev/board/uuid1.jpg")
    private List<String> imageUrls;

    @Schema(description = "게시글의 댓글들, 리스트 형태로 전달", example = "댓글1, 댓글2")
    private List<BoardCommentResponse> comments;

    @Schema(description = "게시글 생성일", example = "2026년06월01일, 22시36분")
    private String createdAt;
}

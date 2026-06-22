package com.naranghiking.chatbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "챗봇에게 전달하기 위한 사용자 질문 DTO")
public record ChatbotRequest(

        @Schema(description = "사용자가 보고 있는 산의 ID", example = "1")
        @Nullable // 일단은 nullable로 진행
        Long mountainId,

        @Schema(description = "사용자의 질문", example = "가볍게 즐기기 좋은 한라산 코스 추천해줘")
        @NotBlank(message = "질문 내용을 입력하세요.")
        @Size(max = 1000, message = "질문은 1000자 이내로 입력해주세요.")
        String message
) {}

package com.naranghiking.chatbot.controller;

import com.naranghiking.chatbot.dto.ChatbotRequest;
import com.naranghiking.chatbot.service.ChatbotService;
import com.naranghiking.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "챗봇에게 질문을 하고, 답변을 받는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Operation(summary = "질문 요청 처리", description = "질문 후 RAG 기반 챗봇이 도출한 답변을 반환")
    @PostMapping
    public ResponseEntity<ApiResult<String>> chat(
            @Parameter(description = "사용자의 id, redis에서 추출", example = "1")
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "사용자의 질문", example = "가볍게 즐기기 좋은 한라산 코스 추천해줘")
            @Valid @RequestBody ChatbotRequest request) {
        String result = chatbotService.chat(userId.toString(), request.message(), request.mountainId());
        return ResponseEntity.ok(ApiResult.success(result));
    }
}

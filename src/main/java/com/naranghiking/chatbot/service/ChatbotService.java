package com.naranghiking.chatbot.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface ChatbotService {
    String chat(String userId, @NotBlank(message = "질문 내용을 입력하세요.") @Size(max = 1000, message = "질문은 1000자 이내로 입력해주세요.") String message, Long mountainId);
}

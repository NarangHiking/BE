package com.naranghiking.chatbot.chain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class IntentRouterChain { // 사용자의 의도 분류 담당

    private final CallLLM callLLM;

    public String analyzeIntent(String userMessage) throws Exception {
        // 사용자의 질문 의도를 분석해서 코스 추천이면 COURSE_SEARCH, 아니면 GENERAL을 리턴하자
        String prompt =
                """
                너는 사용자의 질문이 등산 코스 추천/조회/검색 시스템을 트리거해야 하는지 판별하는 분류기야.
                사용자의 등산 관련 질문의 의도를 분석하고, 결과를 무조건 대문자로만 반환해야 해.
                - 분류 규칙 -
                1. 사용자가 특정 산의 코스 추천, 소요 시간, 난이도, 산의 특징, 교통편 등 '등산 관련 정보' 검색을 원하면: COURSE_SEARCH
                2. 등산과 무관한 인사, 일상 대화, 챗봇의 정체 확인 등 가벼운 잡담이면: GENERAL
                반환값 포맷 예시: COURSE_SEARCH 또는 GENERAL (이외의 텍스트는 절대 출력 금지)
                """;

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", prompt),
                Map.of("role", "user", "content", userMessage)
        );

        return callLLM.call(messages, 0.0, 1);
    }
}

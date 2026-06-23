package com.naranghiking.chatbot.chain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfoFilterChain { // 정보 정제 담당

    private final CallLLM callLLM;

    public String filterContext(String userMessage, String searchResult) throws Exception {
        // 사용자의 질문과 searchResult를 참고해서, 필요한 정보만 남겨서 리턴하자
        String prompt =
                """
                너는 방대한 데이터 소스에서 사용자의 질문에 완벽히 매칭되는 핵심 정보만 발췌하여 요약하는 정보 정제 전문가야.
                아래 제공된 [원본 참고자료]에서 사용자의 요구사항(특정 조건, 난이도 등)에 유용한 등산 코스 내용만 쏙 골라내서 요약해줘.
                만약 유저의 구체적 요구사항과 전혀 맞지 않는 엉뚱한 데이터라면 과감히 버려.
                [원본 참고자료]
                %s
                """.formatted(searchResult);

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", prompt),
                Map.of("role", "user", "content", "유저가 찾는 조건: " + userMessage)
        );

        return callLLM.call(messages, 0.2, 2);
    }
}

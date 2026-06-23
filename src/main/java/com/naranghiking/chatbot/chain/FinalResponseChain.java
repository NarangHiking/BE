package com.naranghiking.chatbot.chain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinalResponseChain { // 최종 답변 생성 담당

    private final CallLLM callLLM;

    public String generateResponse(String mountain, String userMessage, String contextInfo, List<Map<String, String>> histories) throws Exception {
        // 최종적으로 사용자가 조회 중인 산, 메시지, 정제된 정보, 대화 내역을 통해 답변 도출하기
        String prompt =
                """
                너는 전 세계의 모든 산을 탐험한 경험이 있는 친절하고 전문적인 등산 전문가야.
                사용자의 질문에 대해 아래 [조회 중인 산]과 [참고자료], [대화기록]을 기반으로 답변해줘.
                
                [답변 작성 필수 규칙] - 이하 내용 반드시 지킬 것
                1. 형식: 마크다운(##, ** 등)을 절대 사용하지 말고, 읽기 편한 일반 텍스트로 작성할 것.
                2. 분량: 불필요한 말은 빼고 핵심만 담아 적당한 길이로 대답할 것.
                3. 명칭 정확성: 코스를 추천할 때, [참고자료]에 기록된 공식 코스 이름(예: '팔공산_1번코스', '팔공산_2번코스' 등)을 임의로 변형하지 말고 그대로 정확히 출력할 것.
                4. 내용: [참고자료]에 포함된 '산의 전반적인 특징/난이도'와 '코스 정보'를 자연스럽게 엮어서 대답하고, 자잘한 경유지들을 기계적으로 모두 나열하지 말 것.
                5. 정보의 엄격한 통제: 무조건 [참고자료]에 존재하는 정보만으로 답변하고, 개인적으로 알고 있는 다른 산이나 코스, 특징을 절대로 지어내거나 추가하지 말 것.
                
                [조회 중인 산]: %s
                [참고자료]
                %s
                
                [대화기록]
                %s
                """.formatted(mountain, contextInfo, histories);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", prompt));
        messages.addAll(histories);
        messages.add(Map.of("role", "user", "content", userMessage));

        return callLLM.call(messages, 0.7, 3);
    }
}

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
                아래 제공된 [원본 참고자료]에는 'GPX 코스 정보'와 '산림청 제공 명산 정보(난이도, 특징 등)'가 섞여 있을 수 있어.
                사용자의 요구사항을 파악한 뒤, 이 두 가지 정보를 적절히 조합해서 유용한 내용만 쏙 골라내어 요약해줘.
                이때 GPX 코스 정보의 경우 기록된 공식 코스 이름(예: '팔공산_1번코스', '팔공산_2번코스' 등)을 임의로 변형하지 말고 그대로 정확히 유지했으면 좋겠어.
                만약 유저의 요구사항과 전혀 맞지 않는 엉뚱한 산의 데이터라면 과감히 버려.
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

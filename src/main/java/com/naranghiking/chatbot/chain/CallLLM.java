package com.naranghiking.chatbot.chain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallLLM {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gms.api.key}")
    private String gmsKey;
    @Value("${gms.chat-url}")
    private String gmsChatURL;

    public String call(List<Map<String, String>> messages, double temperature, int step) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gmsKey);

        Map<String, Object> body = Map.of(
                "model", "gpt-5.5", // 기존 사용하던 모델과 동일하게 유지
                "messages", messages,
                "temperature", temperature
        );

        // request에 body(사용자 메시지 등), headers(api key 등) 담기
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        // URL로 request를 post 요청으로 보내고 결과를 String으로 받기
        ResponseEntity<String> response = restTemplate.postForEntity(gmsChatURL, request, String.class);
        // 결과에서 content 추출
        String content = objectMapper.readTree(response.getBody())
                .path("choices").get(0)
                .path("message").path("content").asText();

        if(step == 1) { // 1번 단계는 무조건 대문자에, 대문자와 언더바만 남기고 불필요한 것 다 제거
            return content.trim().toUpperCase().replaceAll("[^A-Z_]", "");
        } else { // 2번, 3번 단계는 원문 그대로 전달
            return content;
        }
    }
}

package com.naranghiking.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService{

    private static final String REDIS_KEY_PREFIX = "chat:session:"; // REDIS에 올릴 때 사용할 키의 앞부분
    private static final long SESSION_TIMEOUT_LIMIT = 30; // 대화 내용 기억 시간
    private static final int MAX_HISTORY_SIZE = 10; // 사용자의 질문 5개, 답면 5개 조회

    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${pinecone.api.key}")
    private String pineconeKey; // vector db에 접속하기 위한 api key
    @Value("${gms.embedding-url}")
    private String embeddingURL; // vector db의 임베딩 url

    @Override // 메인 챗봇 실행
    public String chat(String userId, String userMessage) {
        String key = REDIS_KEY_PREFIX + userId; // 사용자의 아이디

        try {
            // 1. Redis에서 이전 대화 기록 가져오기 (문맥 유지) List의 시작(MAX...)부터 끝(-1), redis 문법임
            // jsons의 String에는 json 형식이 통째로 문자열로 들어가있음
            List<String> jsons = redisTemplate.opsForList().range(key, -MAX_HISTORY_SIZE, -1);
            List<Map<String, String>> histories = new ArrayList<>();
            if(jsons != null) {
                for(String json : jsons) { // 값을 하나씩 뽑아서 histories에 넣어주자
                    histories.add(objectMapper.readValue(json, Map.class));
                }
            }

            // 2. 유저 질문을 임베딩(숫자 벡터)으로 변환 (GMS API)
            List<Double> vector = getEmbeddingFromGMS(userMessage); // 유사도 검색을 위해 메시지 임베딩
            String searchResult = queryPinecone(vector); // vector db에서 유사도를 검색, 문장 리턴

            // 3. Pinecone에서 가장 유사한 등산 코스 정보 검색 (Top 2개)

            // 4. 시스템 프롬프트 조립 (검색된 정보 + 이전 대화 기록)
            // 5. GMS(LLM)에 최종 답변 요청

            // 6. Redis에 새로운 대화 내역 업데이트 및 수명(TTL) 30분 연장

        } catch (Exception e) { // 모든 예외에 대해 동일 메시지 전달
            log.error("[ChatbotService] 요청 처리 중 에러 발생 : ", e.getMessage());
            return "죄송합니다. 요청 처리 중 문제가 발생하였습니다.";
        }

        return "";
    }

    private List<Double> getEmbeddingFromGMS(String userMessage) throws Exception { // 사용자 메시지를 벡터값으로 변환(임베딩)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Key", pineconeKey);

        Map<String, Object> body = new HashMap<>();
        body.put("input", userMessage);
        body.put("model", "text-embedding-3-small"); // vector db 구축할 때 사용한 것과 동일 모델

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        // URL로 body랑 header와 함께 POST 요쳥을 보내면 결과값이 String 형식으로 return
        // 에러 발생하면 catch 문으로 이동
        ResponseEntity<String> response = restTemplate.postForEntity(embeddingURL, request, String.class);
        // response를 트리 형태로 만들고, 그 중에서 "data"를 key로 하는 것의 첫 번째 값을 가져온다.
        // 거기서 다시 "embedding"을 key값으로 하는 상자를 가져오면 그곳에 벡터값들이 들어있다.
        JsonNode node = objectMapper.readTree(response.getBody()).path("data").get(0).path("embedding");
        List<Double> vector = new ArrayList<>();
        for (JsonNode val : node) { // 벡터값들을 하나씩 뽑아서 list 형태로 변환
            vector.add(val.asDouble());
        }
        return vector;
    }

    private String queryPinecone(List<Double> vector) {

        return "";
    }
}

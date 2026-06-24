package com.naranghiking.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naranghiking.chatbot.chain.FinalResponseChain;
import com.naranghiking.chatbot.chain.InfoFilterChain;
import com.naranghiking.chatbot.chain.IntentRouterChain;
import com.naranghiking.chatbot.dao.ChatbotDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private static final String REDIS_KEY_PREFIX = "chat:session:"; // REDIS에 올릴 때 사용할 키의 앞부분
    private static final long SESSION_TIMEOUT_LIMIT = 30; // 대화 내용 기억 시간(30분)
    private static final int MAX_HISTORY_SIZE = 10; // 사용자의 질문 5개, 답면 5개 조회
    private static final double SCORE_PIVOT = 0.3;  // 해당 수치보다 낮은 유사도는 엉뚱한 대답으로 간주

    private final ChatbotDao chatbotDao;
    private final IntentRouterChain intentRouterChain;  // 사용자 의도 분류
    private final InfoFilterChain infoFilterChain;      // 정보 정제
    private final FinalResponseChain finalResponseChain;// 최종 답변 생성
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${pinecone.api.key}")
    private String pineconeKey; // vector db에 접속하기 위한 api key
    @Value("${pinecone.url}")
    private String pineconeUrl; // vector db url
    @Value("${gms.api.key}")
    private String gmsKey; // gms api key
    @Value("${gms.embedding-url}")
    private String embeddingURL; // gms의 임베딩 url

    @Override
    public String chat(String userId, String userMessage, Long mountainId) {
        String key = REDIS_KEY_PREFIX + userId; // 사용자의 아이디

        try {
            log.info("[ChatbotService] ===== LLM 체이닝 기반 응답 시작 =====");

            String mountain = chatbotDao.getMountainNameById(mountainId); // 산 id를 통해 사용자가 보고 있는 산의 이름을 반환
            log.info("[ChatbotService] 사용자가 조회 중인 산: {}", mountain);

            // 1. Redis에서 이전 대화 기록 가져오기 (문맥 유지) List의 시작(MAX...)부터 끝(-1), redis 문법임
            // jsons의 String에는 json 형식이 통째로 문자열로 들어가있음
            List<String> jsons = redisTemplate.opsForList().range(key, -MAX_HISTORY_SIZE, -1);
            log.info("[ChatbotService] Redis에서 사용자의 대화 기록 조회 완료");
            List<Map<String, String>> histories = new ArrayList<>();
            if(jsons != null) {
                for(String json : jsons) { // 값을 하나씩 뽑아서 histories에 넣어주자
                    histories.add(objectMapper.readValue(json, Map.class));
                }
            }

            // a. 사용자 의도 분석 (Chainning)
            String intent = intentRouterChain.analyzeIntent(userMessage);
            log.info("[ChatbotService, Chain 1] 사용자 의도 분석 완료: {}", intent);

            String contextInfo = "";

            if("COURSE_SEARCH".equals(intent)) { // 코스 관련 질문일 때만 Pinecone 조회
                // 2. 유저 질문을 임베딩(숫자 벡터)으로 변환 (GMS API)
                List<Double> vector = getEmbeddingFromGMS(mountain + " " + userMessage); // 유사도 검색을 위해 메시지 임베딩
                log.info("[ChatbotService] 사용자의 메시지 임베딩 완료");

                // 3. Pinecone에서 가장 유사한 등산 코스 정보 검색 (Top 8개)
                String searchResult = queryPinecone(vector); // vector db에서 유사도를 검색, 문장 리턴
                log.info("[ChatbotService] 임베딩 메시지의 vector db 유사도 검색 완료");

                // b. 정보 정제 (Chainning)
                contextInfo = infoFilterChain.filterContext(userMessage, searchResult);
                log.info("[ChatbotService, Chain 2] Pinecone에서 조회한 데이터 중 중요한 것만 추출 완료");
                log.info("[ChatbotService, Chain 2] 정제 이후 남은 정보: {}", contextInfo);
            } else {
                log.info("[ChatbotService] 코스 관련 질문이 아니므로, 사용자 메시지 임베딩 생략");
            }

            // c. 최종 답변 생성 (Chainning)
            String answer = finalResponseChain.generateResponse(mountain, userMessage, contextInfo, histories);

            // 4. Redis에 새로운 대화 내역 업데이트 및 수명(TTL) 30분 연장
            String userJson = objectMapper.writeValueAsString(Map.of("role", "user", "content", userMessage));
            String chatbotJson = objectMapper.writeValueAsString(Map.of("role", "assistant", "content", answer));

            // redis의 가장 최근 기록에 사용자의 메시지와 챗봇의 답변 추가
            redisTemplate.opsForList().rightPushAll(key, userJson, chatbotJson);
            // redis의 기록에서 가장 오래된 기록 삭제
            redisTemplate.opsForList().trim(key, -MAX_HISTORY_SIZE, -1);
            // 새롭게 대화가 진행되었으니 세션 만료 시간 초기화
            redisTemplate.expire(key, SESSION_TIMEOUT_LIMIT, TimeUnit.MINUTES);

            return answer;

        } catch (Exception e) {
            log.error("[ChatbotService] 요청 처리 중 에러 발생 : ", e);
            return "죄송합니다. 요청 처리 중 문제가 발생하였습니다.";
        }
    }

    private List<Double> getEmbeddingFromGMS(String userMessage) throws Exception { // 사용자 메시지를 벡터값으로 변환(임베딩)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gmsKey);

        Map<String, Object> body = new HashMap<>();
        body.put("input", userMessage);
        body.put("model", "text-embedding-3-small"); // vector db 구축할 때 사용한 것과 동일 모델

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        // URL로 body랑 header와 함께 POST 요쳥을 보내면 결과값이 String 형식으로 return
        // 에러 발생하면 catch 문으로 이동
        ResponseEntity<String> response = restTemplate.postForEntity(embeddingURL, request, String.class);

        // response를 트리 형태로 만들고, 그 중에서 "data"를 key로 하는 것의 첫 번째 값을 가져온다.
        // 거기서 다시 "embedding"을 key값으로 하는 상자를 가져오면 그곳에 벡터값들이 들어있다.
        JsonNode nodes = objectMapper.readTree(response.getBody()).path("data").get(0).path("embedding");
        List<Double> vector = new ArrayList<>();
        for (JsonNode val : nodes) { // 벡터값들을 하나씩 뽑아서 list 형태로 변환
            vector.add(val.asDouble());
        }
        return vector;
    }

    private String queryPinecone(List<Double> vector) throws Exception { // 임베딩된 사용자 메시지로 vector db에 유사도 검색
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Key", pineconeKey);

        // vector 정보를 넘겨서 그거 기반으로 가장 유사한 코스 2개 조회해보자
        Map<String, Object> body = new HashMap<>();
        body.put("vector", vector);
        body.put("topK", 8); // 가장 유사한 정보 8개 꺼내기
        body.put("includeMetadata", true);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        // pinecone으로 request를 넘겨서 String 타입의 response를 받는다.
        ResponseEntity<String> response = restTemplate.postForEntity(pineconeUrl + "/query", request, String.class);

        // response에서 matches만 꺼내기.
        JsonNode nodes = objectMapper.readTree(response.getBody()).path("matches");
        if(nodes.isEmpty()) return "";

        double maxScore = nodes.get(0).path("score").asDouble(); // nodes에서 가장 높은 유사도 꺼내기
        if(maxScore < SCORE_PIVOT) return "";

        StringBuilder context = new StringBuilder();
        for(JsonNode val : nodes) {
            // val에서 metadata 꺼내고, 그 안에 text를 붙이기
            context.append(val.path("metadata").path("text").asText()).append("\n");
        }

        return context.toString();
    }
}

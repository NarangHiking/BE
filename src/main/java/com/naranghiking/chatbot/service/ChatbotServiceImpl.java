package com.naranghiking.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService{

    private static final String REDIS_KEY_PREFIX = "chat:session:"; // REDIS에 올릴 때 사용할 키의 앞부분
    private static final long SESSION_TIMEOUT_LIMIT = 30; // 대화 내용 기억 시간
    private static final int MAX_HISTORY_SIZE = 10; // 사용자의 질문 5개, 답면 5개 조회

    private final RedisTemplate<String, String> redisTemplate;


    @Override // 메인 챗봇 실행
    public String chat(String userId, String message) {
        String key = REDIS_KEY_PREFIX + userId; //

        try {
            // 1. Redis에서 이전 대화 기록 가져오기 (문맥 유지)


            // 2. 유저 질문을 임베딩(숫자 벡터)으로 변환 (GMS API)

            // 3. Pinecone에서 가장 유사한 등산 코스 정보 검색 (Top 2개)

            // 4. 시스템 프롬프트 조립 (검색된 정보 + 이전 대화 기록)
            // 5. GMS(LLM)에 최종 답변 요청

            // 6. Redis에 새로운 대화 내역 업데이트 및 수명(TTL) 30분 연장

        } catch (Exception e) {
            log.error("[ChatbotService] 요청 처리 중 에러 발생 : ", e.getMessage());
            return "죄송합니다. 요청 처리 중 문제가 발생하였습니다.";
        }

        return "";
    }
}

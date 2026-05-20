package com.naranghiking.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // RedisConnectionFactory = Redis 서버 연결
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 단일 서버 설정
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        // Redis 연결 라이브러리
        return new LettuceConnectionFactory(config);
    }

    // RedisTemplate  데이터 읽기 / 쓰기 도구
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        
        // key를 문자열로 저장
        template.setKeySerializer(new StringRedisSerializer());
        // value를 문자열로 저장
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}

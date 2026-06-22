package com.naranghiking.chatbot.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatbotDao {
    // 산 id를 통해 사용자가 보고 있는 산의 이름을 반환
    String getMountainNameById(Long mountainId);
}

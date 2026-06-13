package com.naranghiking.chatbot.controller;

public class ChatbotController {
    // redis + pinecone 조합으로 진행
    // redis를 통해서 사용자의 대화를 단기로 기억하고, pinecone을 통해서 vector DB 사용.
    // controller -> service -> Spring Ai 라이브러리 -> Pinecone & openAI로 진행
}

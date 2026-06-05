package com.naranghiking.user.controller;

import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.UpdateRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private static String userToken;
    private static String adminToken;
    private static SignUpRequest signUpRequest;

    @BeforeAll
    static void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setPass("test1234");
        signUpRequest.setName("테스트유저");
    }

    @Test
    @DisplayName("전체 유저 조회")
    void selectAll() throws Exception {
        adminToken = jwtUtil.generateAccessToken(1L, "ADMIN");
        mockMvc.perform(get("/user/list")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    @Order(1)
    void register() throws Exception {
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("signup ok"))
                .andDo(print());
    }

    @Test
    @DisplayName("내 정보 조회")
    @Order(2)
    void select() throws Exception {
        userToken = jwtUtil.generateAccessToken(1L, "USER");
        mockMvc.perform(get("/user")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("내 정보 수정")
    @Order(3)
    void update() throws Exception {
        userToken = jwtUtil.generateAccessToken(1L, "USER");
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setPass("newpass1234");
        updateRequest.setName("수정된유저");

        mockMvc.perform(patch("/user")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("ok"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 탈퇴")
    @Order(4)
    void remove() throws Exception {
        userToken = jwtUtil.generateAccessToken(1L, "USER");
        mockMvc.perform(patch("/user/remove")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("ok"))
                .andDo(print());
    }
}
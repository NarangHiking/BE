package com.naranghiking.favorite.controller;

import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.track.dto.Track;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    private static String userToken;

    @BeforeEach
    void setUp() {
        userToken = jwtUtil.generateAccessToken(1L, "USER");
    }

    @Test
    @Order(1)
    @DisplayName("경로 추천 추가")
    public void insert() throws Exception {
        mockMvc.perform(post("/favorite/2")
                .header("Authorization", "Bearer " + userToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("경로 추천 중복")
    public void insert_fail() throws Exception {
        mockMvc.perform(post("/favorite/2")
                .header("Authorization", "Bearer " + userToken))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    @DisplayName("추천 누른 경로 보여주기")
    public void selectFavorites() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/favorite/track")
                .header("Authorization", "Bearer " + userToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("경로 추천 삭제")
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/favorite/2")
                .header("Authorization", "Bearer " + userToken))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

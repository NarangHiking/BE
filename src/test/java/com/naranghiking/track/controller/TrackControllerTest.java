package com.naranghiking.track.controller;

import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.track.dto.Track;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private static String adminToken;
    private static Track track;

    @BeforeAll
    static void setUp() {
        track = new Track();
        track.setId(1L);
        track.setMountainId(5L);
        track.setName("테스트 코스");
        track.setGpxFilePath("/gpx/temp/test.gpx");
    }

    @Test
    void selectByName() throws Exception {
        mockMvc.perform(get("/track/search?name=울산"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    // 산 이름으로 검색
    @DisplayName("산 이름으로 경로 검색")
    @Test
    void selectByConditionMtnName() throws Exception {
        mockMvc.perform(get("/track").param("mtnName", "북한산"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("지역으로 경로 검색")
    @Test
    void selectByConditionLocation() throws Exception {
        mockMvc.perform(get("/track").param("location", "제주"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("고도로 경로 검색")
    @Test
    void selectByConditionHeight() throws Exception {
        mockMvc.perform(get("/track").param("height", "1900"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("복합 검색 2가지로 경로 검색")
    @Test
    void selectByDualCondition() throws Exception {
        mockMvc.perform(get("/track")
                        .param("location", "제주")
                        .param("height", "1900"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("복합 검색 3가지로 경로 검색")
    @Test
    void selectByTripleCondition() throws Exception {
        mockMvc.perform(get("/track")
                        .param("mtnName", "한라")
                        .param("location", "제주")
                        .param("height", "1900"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("복합 검색 - 결과 없음")
    @Test
    void selectByCondition_fail() throws Exception {
        mockMvc.perform(get("/track")
                        .param("mtnName", "테스트")
                        .param("location", "제주")
                        .param("height", "1900"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("아이디로 조회")
    @Test
    void selectById() throws Exception {
        mockMvc.perform(get("/track/16"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
    @DisplayName("아이디로 조회-실패")
    @Test
    void selectById_fail() throws Exception {
        mockMvc.perform(get("/track/17"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("경로 삽입")
    @Order(1)
    void insert() throws Exception {
        adminToken = jwtUtil.generateAccessToken(1L, "ADMIN");
        MvcResult result = mockMvc.perform(post("/track")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(track)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Long id = objectMapper.readTree(response).get("data").get("id").asLong();
        track.setId(id);
    }

    @Test
    @DisplayName("경로 수정")
    @Order(2)
    void update() throws Exception {
        adminToken = jwtUtil.generateAccessToken(1L, "ADMIN");
        track.setMountainId(4L);
        track.setName("수정된 코스");
        track.setGpxFilePath("/gpx/temp/updated.gpx");

        mockMvc.perform(put("/track")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(track)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("수정된 코스"))
                .andDo(print());
    }

    @Test
    @DisplayName("경로 삭제")
    @Order(3)
    void deleteTrack() throws Exception {
        adminToken = jwtUtil.generateAccessToken(1L, "ADMIN");
        mockMvc.perform(delete("/track/" + track.getId())
                        .header("Authorization", "Bearer " + adminToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }
}
package com.naranghiking.mtn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.mtn.dto.Mtn;
import com.naranghiking.mtn.service.MtnService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MtnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MtnService mtnService;

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static Mtn testMtn;

    private String adminToken;
    private String userToken;

    @BeforeAll
    static void setUp() {
        testMtn = new Mtn();
        testMtn.setName("테스트 산");
        testMtn.setLocation("테스트 도");
        testMtn.setHeight(1000);
        testMtn.setDescription("테스트용 산입니다.");
    }

    @BeforeEach
    void setUpToken() {
        adminToken = jwtUtil.generateAccessToken("1", "ADMIN");
        userToken = jwtUtil.generateAccessToken("2", "USER");
    }

    @Test
    @Order(1)
    @DisplayName("산 전체 조회")
    void selectAll() throws Exception {
        mockMvc.perform(get("/mtn/list").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(2)
    @DisplayName("산 추가")
    void insert() throws Exception {
        mockMvc.perform(post("/mtn").header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMtn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("테스트 산"));
    }
    @Test
    @Order(3)
    @DisplayName("산 추가 - 일반유저")
    void userInsert() throws Exception {
        mockMvc.perform(post("/mtn").header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMtn)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    @DisplayName("산 단일 조회")
    void select() throws Exception {
        // insert된 산의 id를 가져옴
        Mtn inserted = mtnService.selectAll().stream()
                .filter(m -> "테스트 산".equals(m.getName()))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/mtn/" + inserted.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("테스트 산"));
    }

    @Test
    @Order(5)
    @DisplayName("존재하지 않는 산 단일 조회 - 실패")
    void selectNotFound() throws Exception {
        mockMvc.perform(get("/mtn/999999"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(6)
    @DisplayName("산 수정")
    void update() throws Exception {
        Mtn inserted = mtnService.selectAll().stream()
                .filter(m -> "테스트 산".equals(m.getName()))
                .findFirst()
                .orElseThrow();

        inserted.setName("수정된 산");
        inserted.setHeight(2000);

        mockMvc.perform(put("/mtn/" + inserted.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inserted)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("수정된 산"));
    }

    @Test
    @Order(7)
    @DisplayName("산 삭제")
    void mtnDelete() throws Exception {
        Mtn inserted = mtnService.selectAll().stream()
                .filter(m -> "수정된 산".equals(m.getName()))
                .findFirst()
                .orElseThrow();
        System.out.println("삭제 요청 id: " + inserted.getId());  // 추가

        mockMvc.perform(delete("/mtn/" + inserted.getId())
                        .header("Authorization", "Bearer " + adminToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());  // 추
    }

    @Test
    @Order(8)
    @DisplayName("삭제 후 조회 - 실패 확인")
    void selectAfterDelete() throws Exception {
        // 삭제된 산은 더 이상 조회되지 않아야 함
        boolean exists = mtnService.selectAll().stream()
                .anyMatch(m -> "수정된 산".equals(m.getName()));
        Assertions.assertFalse(exists);
    }

    @Test
    @Order(9)
    @DisplayName("산 하위 경로 표시")
    void selectPathFromMtn() throws Exception {
        mockMvc.perform(get("/mtn/1/track"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
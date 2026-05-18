package com.naranghiking.controller;

import com.naranghiking.model.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_ok() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\", \"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("login ok"))
                .andReturn();
        User loginUser = (User) result.getRequest().getSession().getAttribute("loginUser");
        assertNotNull(loginUser);
        assertEquals("test@test.com", loginUser.getEmail());
     }


    @Test
    void login_fail_wrong_password() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\", \"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_fail_not_found() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"none@test.com\", \"password\":\"wrong\"}"))
                .andExpect(status().isNotFound());
    }

     @Test
     void logout() throws Exception {
        // given
        login_ok();
        // when
         MvcResult result = mockMvc.perform(post("/api/auth/logout")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andReturn();
        User loginUser = (User) result.getRequest().getSession().getAttribute("loginUser");
        // then
        assertNull(loginUser);
     }

    @Test
    void register_ok() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"woo@chan\", \"password\":\"hwang\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("signup ok"));
    }

    @Test
    void register_fail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\", \"password\":\"hwang\"}"))
                .andExpect(status().isConflict());
    }
}
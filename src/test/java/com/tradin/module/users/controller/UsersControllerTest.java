package com.tradin.module.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.config.TestContainerConfiguration;
import com.tradin.module.users.controller.dto.request.PingRequestDto;
import com.tradin.module.users.service.UsersService;
import com.tradin.module.users.service.dto.PingDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(TestContainerConfiguration.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService usersService;

    @Test
    @WithMockUser
    public void API_AND_SECRET_KEY_유효성_테스트() throws Exception {
        String binanceApiKey = "wrongApiKey";
        String binanceSecretKey = "wrongSecretKey";

        when(usersService.ping(new PingDto(binanceApiKey, binanceSecretKey))).thenReturn("pong");

        mockMvc.perform(post("/v1/users/binance/ping")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new PingRequestDto(binanceApiKey, binanceSecretKey)))
                ).andDo(print())
                .andExpect(status().isOk());
    }
}

package com.yo.webtoon.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yo.webtoon.model.constant.SuccessCode;
import com.yo.webtoon.model.dto.UserDto;
import com.yo.webtoon.model.dto.UserDto.Login;
import com.yo.webtoon.model.dto.UserDto.SignUp;
import com.yo.webtoon.security.TokenProvider;
import com.yo.webtoon.security.WebtoonAccessDeniedHandler;
import com.yo.webtoon.security.WebtoonAuthEntryPoint;
import com.yo.webtoon.service.UserService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private WebtoonAccessDeniedHandler webtoonAccessDeniedHandler;

    @MockBean
    private WebtoonAuthEntryPoint webtoonAuthEntryPoint;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void signUp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/sign-up")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    SignUp.builder()
                        .userId("id")
                        .password("12345678")
                        .userName("name")
                        .role("ROLE_GENERAL").build()
                )))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.successCode").value(SuccessCode.SIGNUP.name()))
            .andExpect(jsonPath("$.message").value(SuccessCode.SIGNUP.getMessage()))
            .andDo(print());
    }

    @Test
    @DisplayName("회원가입 - 존재하지 않는 권한을 입력하는 경우")
    @WithMockUser
    void signUp_INVALID_ROLE() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/sign-up")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    SignUp.builder()
                        .userId("id")
                        .password("12345678")
                        .userName("name")
                        .role("NOT_EXIST_ROLE").build()
                )))
            .andReturn();

        String message = result.getResolvedException().getMessage();
        assertThat(message).contains("유효하지 않은 권한입니다.");
    }

    @Test
    @DisplayName("로그인 - 성공")
    @WithMockUser
    void login() throws Exception {
        given(userService.authenticate(any(UserDto.Login.class))).willReturn("id");
        given(tokenProvider.generateToken("id")).willReturn("jwt-token");

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Login.builder()
                        .userId("id")
                        .password("12345678").build()
                )))
            .andExpect(status().isOk())
            .andExpect(content().string("jwt-token"))
            .andExpect(cookie().value("activeToken", "jwt-token"))
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 - 아이디 입력값 없음")
    @WithMockUser
    void login_BLANK() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Login.builder()
                        .userId("")
                        .password("12345678").build()
                )))
            .andReturn();

        String message = result.getResolvedException().getMessage();
        assertThat(message).contains("아이디를 입력해주세요.");
    }

    @Test
    @DisplayName("회원 조회 - 성공")
    @WithMockUser
    void getUser_SUCCESSS() throws Exception {
        given(userService.getUser(1L))
            .willReturn(UserDto.Get.builder()
                .userId("1")
                .point(100)
                .adultCertificationDate(LocalDate.now())
                .build());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .with(csrf())
                .requestAttr("userId", 1L))
            .andExpect(status().isOk())
            .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        UserDto.Get result = objectMapper.readValue(json, UserDto.Get.class);

        assertThat(result.getUserId()).isEqualTo("1");
        assertThat(result.getPoint()).isEqualTo(100);
        assertThat(result.getAdultCertificationDate()).isEqualTo(LocalDate.now());
    }
}
package com.test.genesis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.genesis.TestInitUtil;
import com.test.genesis.WithMockUser;
import com.test.genesis.domain.user.dto.UserSignRequest;
import com.test.genesis.domain.user.dto.UserUpdateRequest;
import com.test.genesis.security.auth.UserEntityDetail;
import com.test.genesis.security.config.SecurityConfig;
import com.test.genesis.security.filter.JwtAuthenticationFilter;
import com.test.genesis.security.filter.JwtAuthorizationFilter;
import com.test.genesis.security.jwt.JwtAccessToken;
import com.test.genesis.security.jwt.JwtTokenProvider;
import com.test.genesis.service.UserService;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthorizationFilter.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)}
)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtTokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    JwtAccessToken accessToken = new JwtAccessToken(
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQG5hdmVyLmNvbSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpZCI6MSwiZXhwIjoxNjY2NTA5NTYwfQ.rgKWqacMxa0cSRt_d1jx1MjmOiQhd95l-7s-UkHmiVY", new Date());
    @Test
    @DisplayName("회원가입 할 수 있다")
    public void _1() throws Exception {
        UserSignRequest userSignRequest = new UserSignRequest("tes@naver.com", "name", "password", "010-111-111", "USER");
        mockMvc.perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(userSignRequest))
                                .with(csrf().asHeader())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("유저 정보를 수정할 수 있다")
    public void _2() throws Exception {

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("update", "010-2222-2222", "updatePassword");
        mockMvc.perform(
                        put("/user")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(userUpdateRequest))
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .with(csrf().asHeader())
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("토큰을 재발행 할 수 있다.")
    public void _3() throws Exception {

        given(tokenProvider.reissue(any(), any())).willReturn(accessToken);
        mockMvc.perform(
                post("/reissue")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("refreshToken", accessToken.getToken())
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .with(csrf().asHeader())
        ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃 할 수 있다.")
    public void _4() throws Exception {

        mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .with(csrf().asHeader())
        ).andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

}
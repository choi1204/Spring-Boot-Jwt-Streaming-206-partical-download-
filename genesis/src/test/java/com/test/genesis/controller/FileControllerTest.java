package com.test.genesis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.genesis.TestInitUtil;
import com.test.genesis.WithMockUser;
import com.test.genesis.security.config.SecurityConfig;
import com.test.genesis.security.filter.JwtAuthenticationFilter;
import com.test.genesis.security.filter.JwtAuthorizationFilter;
import com.test.genesis.security.jwt.JwtAccessToken;
import com.test.genesis.service.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FileController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthorizationFilter.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)}
)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
public class FileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FileService fileService;

    @Autowired
    ObjectMapper objectMapper;

    JwtAccessToken accessToken = new JwtAccessToken(
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQG5hdmVyLmNvbSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpZCI6MSwiZXhwIjoxNjY2NTA5NTYwfQ.rgKWqacMxa0cSRt_d1jx1MjmOiQhd95l-7s-UkHmiVY", new Date());

    @Test
    @DisplayName("파일 업로드 테스트")
    public void _1() throws Exception {
        MockMultipartFile mockMultipartFile = TestInitUtil.initMockMultipartFile();

        mockMvc.perform(multipart("/file").file("files",mockMultipartFile.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf().asHeader())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
        ).andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("파일 스트리밍 테스트")
    public void _2() throws Exception {
        Resource resource = TestInitUtil.initMockMultipartFile().getResource();
        given(fileService.fileStreaming(any(), any(), any())).willReturn(new ResourceRegion(resource, 0 ,1000));
        mockMvc.perform(get("/file/{fileId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf().asHeader())
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                ).andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}

package com.test.genesis.service;

import com.test.genesis.TestInitUtil;
import com.test.genesis.domain.file.FileEntity;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.repository.FileRepository;
import com.test.genesis.strategy.FileDownload;
import com.test.genesis.strategy.FileUpload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Handler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    FileService fileService;

    @Mock
    FileUpload fileUpload;

    @Mock
    FileDownload fileDownload;

    @Mock
    FileRepository fileRepository;

    UserEntity user = TestInitUtil.createUser();
    @Test
    @DisplayName("upload 할 수 있다.")
    public FileEntity _1() throws IOException {
        MockMultipartFile multipartFile = TestInitUtil.initMockMultipartFile();
        FileEntity fileEntity = TestInitUtil.createFile(user);
        when(fileUpload.upload(multipartFile, user)).thenReturn(fileEntity);
        when(fileRepository.save(fileEntity)).thenReturn(fileEntity);
        FileEntity upload = fileService.upload(multipartFile, user);

        assertThat(upload).usingRecursiveComparison().isEqualTo(fileEntity);
        return upload;
    }

    @Test
    @DisplayName("권한이 있는 유저는 streaming 할 수 있다")
    public void _2() throws IOException {
        Resource resource = TestInitUtil.initMockMultipartFile().getResource();
        FileEntity fileEntity = _1();
        when(fileRepository.findEntityGraphById(any())).thenReturn(Optional.of(fileEntity));
        when(fileDownload.streaming(any(),any())).thenReturn(new ResourceRegion(resource, 0, 1000));
        HttpHeaders httpHeaders = new HttpHeaders();
        ResourceRegion resourceRegion = fileService.fileStreaming(fileEntity.getId(), user, httpHeaders);
        assertThat(resourceRegion).isNotNull();
    }

    @Test
    @DisplayName("권한이 없는 유저는 streaming 할 수 없다")
    public void _3() throws IOException {
        UserEntity newUser = TestInitUtil.createUser();
        FileEntity fileEntity = _1();
        when(fileRepository.findEntityGraphById(any())).thenReturn(Optional.of(fileEntity));
        HttpHeaders httpHeaders = new HttpHeaders();
        assertThrows(RuntimeException.class, () -> fileService.fileStreaming(fileEntity.getId(), newUser, httpHeaders));
    }

    @Test
    @DisplayName("관리자는 streaming 할 수 없다")
    public void _4() throws IOException {
        Resource resource = TestInitUtil.initMockMultipartFile().getResource();
        UserEntity admin = TestInitUtil.createAdmin();
        FileEntity fileEntity = _1();
        when(fileDownload.streaming(any(),any())).thenReturn(new ResourceRegion(resource, 0, 1000));
        when(fileRepository.findEntityGraphById(any())).thenReturn(Optional.of(fileEntity));
        HttpHeaders httpHeaders = new HttpHeaders();
        ResourceRegion resourceRegion = fileService.fileStreaming(fileEntity.getId(), admin, httpHeaders);
        assertThat(resourceRegion).isNotNull();
    }

    @Test
    @DisplayName("없는 파일 조회시 에러발생")
    public void _5() throws IOException {
        FileEntity fileEntity = TestInitUtil.createFile(user);
        when(fileRepository.findEntityGraphById(any())).thenReturn(Optional.empty());
        HttpHeaders httpHeaders = new HttpHeaders();
        assertThrows(RuntimeException.class, () -> fileService.fileStreaming(fileEntity.getId(), user, httpHeaders));
    }
}
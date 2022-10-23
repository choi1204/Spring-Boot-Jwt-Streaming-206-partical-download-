package com.test.genesis.service;

import com.test.genesis.TestInitUtil;
import com.test.genesis.domain.file.FileEntity;
import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.strategy.FileDownload;
import com.test.genesis.strategy.FileUpload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileUtilTest {

    FileUpload fileUpload = new LocalFileUpload();

    FileDownload fileDownload = new PartialFileDownload();

    @Test
    @DisplayName("파일 업로드 테스트")
    public FileEntity upload() throws IOException {

        UserEntity userEntity = TestInitUtil.createUser();

        MockMultipartFile file = TestInitUtil.initMockMultipartFile();
        FileEntity upload = fileUpload.upload(file, userEntity);
        assertThat(upload.getMimeType()).isEqualTo("video/mp4");
        assertThat(upload.getFileUrl()).contains(fileUpload.getUploadPath());
        return upload;
    }

    @Test
    @DisplayName("파일 업로드 테스트 실패")
    public void upload_ail() throws IOException {

        UserEntity userEntity = TestInitUtil.createUser();

        String url = "fail.mp4";
        assertThrows(FileNotFoundException.class, () ->
                new MockMultipartFile("video", "test.mp4", "video/mp4", new FileInputStream(url)));
    }

    @Test
    @DisplayName("파일 다운로드")
    public void streaming() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        FileEntity upload = upload();
        ResourceRegion streaming = fileDownload.streaming(headers, upload);

        assertThat(streaming).isNotNull();
    }


}
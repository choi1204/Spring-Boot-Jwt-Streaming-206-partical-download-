package com.test.genesis.strategy;

import com.test.genesis.domain.file.FileEntity;
import com.test.genesis.domain.user.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;

public interface FileUpload {
    FileEntity upload(MultipartFile multipartFile, UserEntity userEntity);

    String getUploadPath();
}

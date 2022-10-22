package com.test.genesis.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;

public interface FileUpload {
    String upload(MultipartFile multipartFile);
}

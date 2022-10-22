package com.test.genesis.security.service;

import com.test.genesis.domain.file.FileEntity;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.repository.FileRepository;
import com.test.genesis.strategy.FileDownload;
import com.test.genesis.strategy.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;

    private final FileUpload fileUpload;

    private final FileDownload fileDownload;

    @Transactional
    public void upload(MultipartFile multipartFile, UserEntity userEntity) {
        String url = fileUpload.upload(multipartFile);

        FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename(), url, multipartFile.getContentType(), multipartFile.getSize(), userEntity);
        fileRepository.save(fileEntity);
    }

    public ResourceRegion fileStreaming(Long id, HttpHeaders httpHeaders) throws IOException {
        FileEntity fileEntity = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 파일 id입니다."));
        return fileDownload.streaming(httpHeaders, fileEntity);
    }


}

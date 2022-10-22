package com.test.genesis.security.service;

import com.test.genesis.domain.file.FileEntity;
import com.test.genesis.domain.user.Role;
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
import java.rmi.RemoteException;

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

    private FileEntity getFile(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new RuntimeException("없는 file입니다."));
    }
    public ResourceRegion fileStreaming(Long fileId, Long userId, HttpHeaders httpHeaders) throws IOException {
        FileEntity fileEntity = getFile(fileId);
        if (!checkUser(fileEntity.getUserEntity(), userId)) {
            throw new RuntimeException("잘못된 유저 접근입니다.");
        }
        return fileDownload.streaming(httpHeaders, fileEntity);
    }

    public boolean checkUser(UserEntity userEntity, Long checkId) {
        Role role = userEntity.getRole();
        switch(role) {
            case USER -> {
                if (userEntity.getId().equals(checkId)) {
                    return true;
                } else {
                    return false;
                }
            }
            case ADMIN -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }


}

package com.test.genesis.service;

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

import java.io.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;

    private final FileUpload fileUpload;

    private final FileDownload fileDownload;

    @Transactional
    public FileEntity upload(MultipartFile multipartFile, UserEntity userEntity) {
        FileEntity fileEntity = fileUpload.upload(multipartFile, userEntity);
        return fileRepository.save(fileEntity);
    }

    public Long getIdByFileName(String fileName) {
        return fileRepository.findEntityGraphByFileName(fileName).orElseThrow(() -> new RuntimeException("없는 file입니다.")).getId();
    }
    private FileEntity getFile(Long fileId) {
        return fileRepository.findEntityGraphById(fileId).orElseThrow(() -> new RuntimeException("없는 file입니다."));
    }
    public ResourceRegion fileStreaming(Long fileId, UserEntity user, HttpHeaders httpHeaders) throws IOException {
        FileEntity fileEntity = getFile(fileId);
        if (!checkUser(user, fileEntity.getUserEntity().getId())) {
            throw new RuntimeException("잘못된 유저 접근입니다.");
        }
        return fileDownload.streaming(httpHeaders, fileEntity);
    }

    public ResourceRegion fileStreamingTest(Long fileId, HttpHeaders httpHeaders) throws IOException {
        FileEntity fileEntity = getFile(fileId);
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

package com.test.genesis.service;

import com.test.genesis.domain.file.FileEntity;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.strategy.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class LocalFileUpload implements FileUpload {

    private final String uploadPath = "C:\\Users\\choih\\OneDrive\\문서\\GitHub\\Genesis_Lab_Tes\\genesis\\src\\main\\resources\\files";

    public String getUploadPath() {
        return uploadPath;
    }

    public FileEntity upload(MultipartFile multipartFile, UserEntity userEntity) {
        String originalName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + originalName.substring(originalName.lastIndexOf("\\") + 1);

        Path path = Paths.get(uploadPath).resolve(fileName);
        try {
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패하였습니다." + e.getMessage());
        }
        return FileEntity.builder()
                .fileName(fileName)
                .fileUrl(path.toString())
                .mimeType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .userEntity(userEntity)
                .build();

    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("//", File.separator);
        File fileFolder = new File(uploadPath, folderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        return folderPath;
    }
}

package com.test.genesis.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class LocalFileUpload implements FileUpload{

    private final String uploadPath = "C:\\upload";

    public String upload(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
        String uuid = UUID.randomUUID().toString();
        String folderUrl = makeFolder();
        String saveUrl = uploadPath + File.separator + folderUrl + File.separator + uuid + "_" + fileName;
        Path path = Paths.get(saveUrl);
        try {
            multipartFile.transferTo(path);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패하였습니다." + e.getMessage());
        }
        return saveUrl;
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

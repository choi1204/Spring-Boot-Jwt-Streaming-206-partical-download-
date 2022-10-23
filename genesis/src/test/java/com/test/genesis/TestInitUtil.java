package com.test.genesis;

import com.test.genesis.domain.file.FileEntity;
import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

public class TestInitUtil {
    static Long userId = 0L;
    static Long fileId = 0L;

    public static UserEntity createUser() {
        String email = "test@naver.com";
        String name = "choi";
        String phoneNumber = "010-0000-0000";
        String password = "password";
        Role role = Role.USER;
        return UserEntity.builder().id(userId++).email(email).name(name).phoneNumber(phoneNumber).password(password).role(role).build();
    }

    public static UserEntity createAdmin() {
        String email = "admin@naver.com";
        String name = "choi";
        String phoneNumber = "010-0000-0000";
        String password = "password";
        Role role = Role.ADMIN;
        return UserEntity.builder().email(email).name(name).phoneNumber(phoneNumber).password(password).role(role).build();
    }

    public static FileEntity createFile(UserEntity userEntity) {
        String fileName = "fileName";
        String fileUrl = "fileUrl";
        String mimeType = "mimeType";
        int size = 100;
        return new FileEntity(fileId++, fileName, fileUrl, mimeType, size, userEntity);
    }

    public static MockMultipartFile initMockMultipartFile() throws IOException {
        String url = "C:\\Users\\choih\\OneDrive\\문서\\GitHub\\Genesis_Lab_Tes\\genesis\\src\\main\\resources\\files\\7482edb8-c75d-4f11-96a9-42a072875084_test.mp4";
        return new MockMultipartFile("video", "test.mp4", "video/mp4", new FileInputStream(url));
    }

}

package com.test.genesis.domain.file;

import com.test.genesis.domain.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileEntityTest {

    @Test
    @DisplayName("파일을 생성할 수 있다.")
    public void _1() {
        UserEntity userEntity = UserEntity.builder().build();
        String fileName = "fileName";
        String fileUrl = "fileUrl";
        String mimeType = "mimeType";
        int size = 100;
        FileEntity fileEntity = new FileEntity(fileName, fileUrl, mimeType, size, userEntity);

        assertThat(fileEntity.getFileName()).isEqualTo(fileName);
        assertThat(fileEntity.getFileUrl()).isEqualTo(fileUrl);
        assertThat(fileEntity.getMimeType()).isEqualTo(mimeType);
        assertThat(fileEntity.getSize()).isEqualTo(size);
        assertThat(fileEntity.getUserEntity()).isEqualTo(userEntity);
    }
}
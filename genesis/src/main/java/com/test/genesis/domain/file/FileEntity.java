package com.test.genesis.domain.file;

import com.test.genesis.domain.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String fileUrl;
    @Column(nullable = false)
    private String mimeType;
    @Column(nullable = false)
    private long size;

    @Builder
    public FileEntity(Long id, String fileName, String fileUrl, String mimeType, long size, UserEntity userEntity) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.mimeType = mimeType;
        this.size = size;
        this.userEntity = userEntity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;
}

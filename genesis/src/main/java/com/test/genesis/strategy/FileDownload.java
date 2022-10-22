package com.test.genesis.strategy;

import com.test.genesis.domain.file.FileEntity;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

public interface FileDownload {
    ResourceRegion streaming(HttpHeaders httpHeaders, FileEntity fileEntity) throws IOException;
}

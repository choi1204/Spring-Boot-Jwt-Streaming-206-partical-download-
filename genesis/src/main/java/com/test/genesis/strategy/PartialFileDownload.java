package com.test.genesis.strategy;

import com.test.genesis.domain.file.FileEntity;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
@Component

public class PartialFileDownload implements FileDownload{

    @Override
    public ResourceRegion streaming(HttpHeaders httpHeaders, FileEntity fileEntity) throws IOException {
        FileUrlResource fileUrlResource = new FileUrlResource(fileEntity.getFileUrl());
        return resourceRegion(fileUrlResource, httpHeaders);
    }

    private ResourceRegion resourceRegion(Resource resource, HttpHeaders httpHeaders) throws IOException {
        final long chunkSize = 1000000L;
        long contentLength = resource.contentLength();

        if (httpHeaders.getRange().isEmpty()) {
            return new ResourceRegion(resource, 0, Long.min(chunkSize, contentLength));
        }

        HttpRange httpRange = httpHeaders.getRange().stream().findFirst().get();
        long start = httpRange.getRangeStart(contentLength);
        long end = httpRange.getRangeEnd(contentLength);
        long rangeLength = Long.min(chunkSize, end - start + 1);
        return new ResourceRegion(resource, start, rangeLength);
    }
}

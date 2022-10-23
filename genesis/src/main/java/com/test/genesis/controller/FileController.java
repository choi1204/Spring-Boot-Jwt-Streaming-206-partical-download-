package com.test.genesis.controller;

import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.security.annotation.LoginUser;
import com.test.genesis.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class FileController {

    private final FileService fileService;
    @PostMapping
    public ResponseEntity<Void> upload(@RequestParam MultipartFile[] files, @LoginUser UserEntity userEntity) {

        for (MultipartFile file : files) {
            fileService.upload(file, userEntity);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<ResourceRegion> stream(@RequestHeader HttpHeaders httpHeaders, @PathVariable Long fileId, @LoginUser UserEntity userEntity) throws IOException {

        ResourceRegion resourceRegion = fileService.fileStreaming(fileId, userEntity, httpHeaders);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(resourceRegion.getResource()).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegion);
    }





}

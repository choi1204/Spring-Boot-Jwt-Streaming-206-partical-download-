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

import java.io.IOException;
@RestController
@RequiredArgsConstructor
@RequestMapping("/view/file")
public class ViewRestController {
        private final FileService fileService;

        @GetMapping("/{fileId}")
        public ResponseEntity<ResourceRegion> stream(@RequestHeader HttpHeaders httpHeaders, @PathVariable Long fileId) throws IOException {

            ResourceRegion resourceRegion = fileService.fileStreamingTest(fileId, httpHeaders);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaTypeFactory.getMediaType(resourceRegion.getResource()).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resourceRegion);
        }
}

package com.test.genesis.controller;

import com.test.genesis.domain.file.FileEntity;
import com.test.genesis.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * RestApi활용해서 비디오 upload시 모든 video resource/files로 저장.
 * view 테스트 시 권한 상관없이 upload된 video partical download 실행.
 * 임의로 resource/files/에 동영상 삽입시 에러 발생.
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final FileRepository fileRepository;

    private final String uploadPath = "C:\\Users\\choih\\OneDrive\\문서\\GitHub\\Genesis_Lab_Tes\\genesis\\src\\main\\resources\\files";

    @GetMapping("/")
    String index(Model model) throws IOException {

        PathResource resource = new PathResource(uploadPath);

        Object[] videos = Files.list(Paths.get(resource.getURI()))
                .map(f -> f.getFileName().toString())
                .toArray();
        model.addAttribute("videos", videos);
        return "index";
    }

    @GetMapping("/{videoName}")
    public String video(@PathVariable String videoName, Model model){
        Long id = fileRepository.findEntityGraphByFileName(videoName).get().getId();
        model.addAttribute("fileId", id);
        return "file";
    }
}

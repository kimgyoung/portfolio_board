package com.example.demo.controller;

import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class FileDownloadController {
    private final BoardService boardService;

    @GetMapping("/download/{uuid}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuid, @PathVariable String fileName){
        Path filePath = Paths.get("C:/Users/G/Desktop/demo/boardFile/" + uuid + fileName);

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists() || resource.isReadable()){
                return ResponseEntity.ok()
                        .header("Content-Disposition"
                                , "attachMent; filename=\""
                                + resource.getFilename()
                                + "\"").body(resource);
            }
            else {
                return ResponseEntity.notFound().build();
            }

        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    // 파일 삭제 요청을 처리하는 부분을 수정하겠습니다.
    @GetMapping("file/delete/{boardId}")
    public String fileDelete(@PathVariable("boardId") Long boardId){
        boardService.deleteFile(boardId);
        return "redirect:/"; // 파일 삭제 후 리다이렉트할 경로. 이 부분은 실제 프로젝트에 맞게 수정해 주세요.
    }
}

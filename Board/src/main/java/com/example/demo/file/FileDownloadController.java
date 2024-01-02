package com.example.demo.file;

import com.example.demo.board.BoardService;
import com.example.demo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class FileDownloadController {
    private final BoardService boardService;

    // "C:/Users/G/Desktop/portfolio_board/boardFile/";
    // "C:/Users/김가영/Desktop/portfolio_board/Board/boardFile/";

    // 주어진 UUID와 파일명으로 파일을 찾아 반환
    @GetMapping("/download/{uuid}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuid, @PathVariable String fileName){
        // 파일 경로
        Path filePath = Paths.get("C:/Users/G/Desktop/portfolio_board/boardFile/" + uuid + fileName);
        try {
            // 파일 경로로 UrlResource 생성
            Resource resource = new UrlResource(filePath.toUri());
            // 리소스가 존재하고 읽을 수 있다면, 해당 파일을 반환
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

    // 특정 게시글에 첨부 된 파일 삭제
    @GetMapping("/file/delete/{boardId}")
    public String fileDelete(@PathVariable("boardId") Long boardId,
                             @AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getUser().getId();
        boardService.deleteFile(boardId,userId);
        return "redirect:/";
    }
}

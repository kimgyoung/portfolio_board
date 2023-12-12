package com.example.demo.board;

import com.example.demo.file.BoardFile;
import com.example.demo.file.FileRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileRepository fileRepository;

    // create 로 이동
    @GetMapping("/create")
    public String create(){
        return "create";
    }

    // model <-> html
    @GetMapping(value = {"/paging", "/"})
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){

        Page<BoardDto> boards = boardService.paging(pageable);

        int blockLimit = 3;
        int startPage = (int)Math.ceil((double)pageable.getPageNumber() / blockLimit - 1) * blockLimit + 1;
        int endPage = (startPage+ blockLimit - 1) < boards.getTotalPages() ? (startPage + blockLimit -1) : boards.getTotalPages();

        model.addAttribute("boardList", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }

    // 변경전 게시글 데이터를 가져 와서 update로 넘겨 줌
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){

        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("board", boardDto);

        return "update";
    }

    // 변경 된 게시글 데이터를 받아 와서 저장
    @PostMapping("update")
    public String update(@ModelAttribute BoardDto boardDto,
                         @RequestParam(value = "newFiles", required = false) MultipartFile[] newFiles) throws IOException {
        boardService.update(boardDto, newFiles);
        return "redirect:/board/";
    }

    // 글 목록 중 한 줄 씩 불러 온 후 상세 정보로
    @GetMapping("/{id}")
    public String paging(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable){
        BoardDto boardDto = boardService.findById(id);

        model.addAttribute("board", boardDto);
        model.addAttribute("page", pageable.getPageNumber());

        List<BoardFile> byBoardFiles = fileRepository.findByBoardId(id);
        model.addAttribute("files",byBoardFiles);

        return "detail";
    }

    // 입력 받은 데이터 글 저장
    @PostMapping("/save")                                               // 파일 불러 오기
    public String save(@ModelAttribute BoardDto boardDto,
                       @RequestParam MultipartFile[] files,
                       @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        boardDto.setCreateTime(LocalDateTime.now());
        boardService.save(boardDto, files);
        return "redirect:/board/";
    }

    // id 값 가져 와서 게시 글 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        boardService.deleteFile(id);
        return "home";
    }

}

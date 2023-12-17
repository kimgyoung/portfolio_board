package com.example.demo.board;

import com.example.demo.file.BoardFile;
import com.example.demo.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;
    private final FileRepository fileRepository;

    @GetMapping("/create")
    public String create() {
        return "create";
    }

    // 입력 받은 데이터, 글 저장
    // BoardDto: 게시글 데이터를 담은 DTO 객체
    // 파일 불러 오기 (required=false) : 파일이 없을 때도 요청을 처리
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDto boardDto,
                       @RequestParam (required=false) MultipartFile[] files) throws IOException {
        boardService.save(boardDto, files);
        return "redirect:/board/";
    }

    // 게시글 목록 페이지
    // 페이징 정보를 담은 Pageable 객체 // 뷰에 전달할 데이터를 담은 Model 객체
    @GetMapping(value = {"/paging", "/"})
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDto> boards = boardService.paging(pageable);

        int blockLimit = 3; // 한번에 보여줄 페이징 블록의 개수
        int startPage = (int)Math.ceil((double)pageable.getPageNumber() / blockLimit - 1) * blockLimit + 1; // 1
        int endPage = (startPage+ blockLimit - 1) < boards.getTotalPages() ? (startPage + blockLimit -1) : boards.getTotalPages(); // 3

        // 뷰에 전달
        model.addAttribute("boardList", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";
    }

    // 변경 전 특정 게시글 데이터를 가져 와서 update로 넘겨 줌
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

    // 특정 게시글의 상세 정보를 보여주는 페이지로 이동
    @GetMapping("/{id}")
    public String paging(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable){
        BoardDto boardDto = boardService.findById(id);

        model.addAttribute("board", boardDto);
        model.addAttribute("page", pageable.getPageNumber());

        List<BoardFile> byBoardFiles = fileRepository.findByBoardId(id);
        model.addAttribute("files",byBoardFiles);

        return "detail";
    }

    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "home";
    }

}

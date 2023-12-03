package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.FileDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardFile;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    private final String filePath = "C:/Users/G/Desktop/demo/boardFile/";

    // 가져온 데이터 DB에 저장
    @Transactional
    public void save(BoardDto boardDto, MultipartFile[] files) throws IOException {
        boardDto.setCreateTime(LocalDateTime.now());

        // 내가 어디다 저장을 할 건지에 대한 경로
        // 학원 에서는 /G/ || 집 에서는 본인 PC 이름
        Path uploadPath = Paths.get(filePath);
        // 만약 경로가 없다면, 경로 생성
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        // 게시글 DB에 저장 후 pk를 받아 옴
        Long id = boardRepository.save(boardDto.toEntity()).getId();
        Board board = boardRepository.findById(id).get();

        // 파일 정보 저장
        for(MultipartFile file : files) {
            if (!file.isEmpty()){
                // 파일명 추출
                String originalFileName = file.getOriginalFilename();
                // 확장자 추출
                String formatType = originalFileName.substring(
                        originalFileName.lastIndexOf("."));

                // UUID 생성
                String uuid = UUID.randomUUID().toString();

                // 경로 지정
                // C:/Users/G/Desktop/demo/boardFile/{uuid + originalFileName}
                String path = filePath + uuid + originalFileName;

                // 경로에 파일을 저장. (NO DB)
                file.transferTo(new File(path));

                BoardFile boardFile = BoardFile.builder()
                        .filePath(filePath)
                        .fileName(originalFileName)
                        .fileType(formatType)
                        .uuid(uuid)
                        .fileSize(file.getSize())
                        .board(board)
                        .build();

                fileRepository.save(boardFile);
            }
        }
        // 파일이 하나 이상 추가되었다면 fileExists를 true로 설정
        if (files.length > 0 && files[0].getSize() > 0) {
            board.setFileExists(true);
            boardRepository.save(board);  // 변경된 상태를 DB에 저장
        }
    }

    // 페이지 리스트를 가져와서 보여 줌
    public Page<BoardDto> paging(Pageable pageable){

        // 페이지 시작 번호 세팅
        int page = pageable.getPageNumber() -1 ;
        // 페이지에 포함 될 게시물 개수
        int size = 5;
        // 전체 게시물 불러옴
        Page<Board> boards = boardRepository.findAll(
                // 정렬 처리 해서 가져 옴(자동)
                PageRequest.of(page, size));
        // Board를 BoardDto로 매핑하면서, boardFiles도 FileDto 리스트로 변환하여 처리함
        return boards.map(board -> {
            // board의 boardFiles를 FileDto 리스트로 변환
            List<FileDto> fileDtos = board.getBoardFiles().stream()
                    .map(FileDto::toFileDto)
                    .collect(Collectors.toList());

            // BoardDto 객체 생성시 boardFiles도 함께 생성자에 넘겨줌
            return new BoardDto(
                    board.getId(),
                    board.getTitle(),
                    board.getUsername(),
                    board.getContents(),
                    board.getCreateTime(),
                    board.getUpdateTime(),
                    board.getFileExists(),
                    fileDtos  // 'fileDtos'를 생성자에 추가함
            );
        });
    }

    public BoardDto findById(Long id) {
        if(boardRepository.findById(id).isPresent()){
            Board board = boardRepository.findById(id).get();
            return BoardDto.toBoardDto(board);
        }
        return null;
    }

    @Transactional
    public void update(BoardDto boardDto, MultipartFile[] newFiles) throws IOException {
        if(boardRepository.findById(boardDto.getId()).isPresent()){
            Optional<Board> boardOptional = boardRepository.findById(boardDto.getId());
            Board board = boardOptional.get();

            // 새로운 파일이 있다면 기존 파일을 삭제하고 새 파일을 업로드
            if (newFiles != null && newFiles.length > 0 && !newFiles[0].isEmpty()) {
                // 기존에 첨부되어 있던 파일 삭제
                List<BoardFile> oldFiles = fileRepository.findByBoardId(board.getId());
                for (BoardFile oldFile : oldFiles) {
                    deleteFile(board.getId());  // 기존 파일 삭제
                }

                // 새로운 파일을 저장하고 DB에 저장
                save(boardDto, newFiles);
            }

            // 게시글 정보를 업데이트하고 DB에 저장
            board.updateFromDto(boardDto);
            boardRepository.save(board);
        }
    }


    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }


    public BoardFile getFileByBoardId(Long boardId) {
        List<BoardFile> boardFiles = fileRepository.findByBoardId(boardId);
        if (!boardFiles.isEmpty()) {
            return boardFiles.get(0);  // 첫 번째 파일만 반환
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteFile(Long boardId){
        BoardFile boardFile = getFileByBoardId(boardId);

        // 실제 파일 시스템에서의 파일 경로
        String filePath = "C:/Users/G/Desktop/demo/boardFile/" + boardFile.getUuid()+ boardFile.getFileName();

        File file = new File(filePath);

        if(file.exists()){
            if(file.delete()){
                System.out.println("파일이 삭제되었습니다.");
            }else{
                System.out.println("파일 삭제 실패");
            }
        }else{
            System.out.println("파일이 존재하지 않습니다.");
        }

        // DB에서의 파일 정보 삭제
        try {
            fileRepository.deleteById(boardFile.getId());

            // 게시글에 첨부된 모든 파일이 삭제되었다면 fileExists를 false로 설정
            Board board = boardFile.getBoard();
            if (fileRepository.findByBoardId(board.getId()).isEmpty()) {
                board.setFileExists(false);
                boardRepository.save(board);  // 변경된 상태를 DB에 저장
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DB에서 파일 정보를 삭제 하는 데 실패 했습니다.");
        }
    }
}

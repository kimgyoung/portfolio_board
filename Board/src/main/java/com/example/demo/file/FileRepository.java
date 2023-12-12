package com.example.demo.file;

import com.example.demo.file.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<BoardFile, Long> {
    // 보드를 확인 해서 보드 반환
    List<BoardFile> findByBoardId(Long boardId);
}

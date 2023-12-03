package com.example.demo.repository;

import com.example.demo.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<BoardFile, Long> {
    // 보드를 확인 해서 보드 반환
    List<BoardFile> findByBoardId(Long boardId);
}

package com.example.demo.board;

import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 기능을 구현 해 주는 JpaRepository , Board Entity, Long 타입 id
public interface BoardRepository extends JpaRepository<Board, Long> {

}

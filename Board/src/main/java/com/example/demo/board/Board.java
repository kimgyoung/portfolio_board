package com.example.demo.board;

import com.example.demo.file.BoardFile;
import com.example.demo.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45)
    private String username;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 256, nullable = false)
    private String contents;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean fileExists;

    // mappedBy = "board": comment 클래스에 있는 변수 명(board)
    // cascade = CascadeType.REMOVE: 게시물이 삭제 되면 댓글을 자동 으로 지워 줌
    // orphanRemoval = true: 연결 관계가 끊어 지면 자동 삭제
    // fetch = FetchType.LAZY: 지연 로딩(성능 최적화)
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFile> boardFiles = new ArrayList<>();

    @Builder
    public Board(Long id, String username, String title, String contents, LocalDateTime createTime, LocalDateTime updateTime, Boolean fileExists){
        this.id = id;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.fileExists = fileExists;
    }

    // 모든 변경 사항을 셋팅
    public void updateFromDto(BoardDto boardDto){
        this.title = boardDto.getTitle();;
        this.contents = boardDto.getContents();
        this.username = boardDto.getUsername();
        this.createTime = boardDto.getUpdateTime();
    }

    public void setFileExists(Boolean fileExists) {
        this.fileExists = fileExists;
    }
}


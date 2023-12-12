package com.example.demo.comment;

import com.example.demo.board.Board;
import com.example.demo.comment.CommentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 비소유 지만 확인은 됨
    // 비소유 지만 보드를 fk로 가지고 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String writer;

    private String contents;

    private LocalDateTime createTime;

    @Builder
    public Comment(Long id, String writer, String contents, Board board, LocalDateTime createTime){
        this.id = id;
        this.writer = writer;
        this.contents = contents;
        this.board = board;
        this.createTime = createTime;
    }

    public void updateComment (CommentDto commentDto) {
        this.contents = commentDto.getContents();
        //this.createTime =commentDto.getCreateTime(LocalDateTime.now()) ;
    }
}

package com.example.demo.dto;

import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
//* @ToString
@Setter
@Getter
public class CommentDto {

    private Long id;

    private String writer;

    private String contents;

    private Long BoardId;

    private LocalDateTime createTime;

    public Comment toEntity(Board board){
        return Comment.builder()
                .id(id)
                .writer(writer)
                .contents(contents)
                .board(board)
                .createTime(LocalDateTime.now())
                .build();
    }

    // Comment 객체를 CommentDto 객체로 변환하는 메서드
    public static CommentDto fromEntity(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getWriter(),
                comment.getContents(),
                comment.getBoard().getId(),
                comment.getCreateTime()
        );
    }
    // contents 필드만을 가진 생성자
    public CommentDto(String contents) {
        this.contents = contents;
    }

/*
    public LocalDateTime getCreateTime(LocalDateTime now) {
        return now;
    }

 */
}
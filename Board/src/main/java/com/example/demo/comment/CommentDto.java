package com.example.demo.comment;

import com.example.demo.board.Board;
import com.example.demo.user.User;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentDto {

    private Long id;

    private String writer;

    private String contents;

    private Long BoardId;

    private LocalDateTime createTime;

    private Long UserId;

    public Comment toEntity(Board board, User user){
        return Comment.builder()
                .id(id)
                .writer(writer)
                .contents(contents)
                .board(board)
                .createTime(LocalDateTime.now())
                .user(user)
                .build();
    }

    // Comment 객체를 CommentDto 객체로 변환하는 메서드
    public static CommentDto fromEntity(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getWriter(),
                comment.getContents(),
                comment.getBoard().getId(),
                comment.getCreateTime(),
                comment.getUser().getId()
        );
    }
    // contents 필드만을 가진 생성자 (댓글 내용 업데이트를 위해 필요)
    public CommentDto(String contents) {
        this.contents = contents;
    }
}
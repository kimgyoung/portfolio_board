package com.example.demo.comment;

import com.example.demo.comment.CommentDto;
import com.example.demo.board.Board;
import com.example.demo.comment.Comment;
import com.example.demo.board.BoardRepository;
import com.example.demo.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Comment save(CommentDto commentDto) {
        Optional<Board> optionalBoard =
                boardRepository.findById(commentDto.getBoardId());

        if(optionalBoard.isPresent()){
            Board foundBoard  = optionalBoard.get();
            Comment comment = commentDto.toEntity(foundBoard);
            return commentRepository.save(comment);
        } else {
            return null;
        }
    }

    public List<CommentDto> getCommentsByBoardId(Long boardId) {
        List<Comment> commentList = commentRepository.findByBoardId(boardId);
        return commentList.stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Comment deleteComment(Long commentId) {
        Comment deletedComment = commentRepository.findById(commentId).orElse(null);
        if (deletedComment != null) {
            commentRepository.deleteById(commentId);
        }
        return deletedComment;
    }

    @Transactional
    public Comment updateComment(Long commentId, String contents) {
        Optional<Comment> optionalComment =
                commentRepository.findById(commentId);
        if(optionalComment.isPresent()){
            Comment comment = optionalComment.get();
            CommentDto commentDto = new CommentDto(contents);
            comment.updateComment(commentDto);
            return comment;
        }
        return null;
    }
}

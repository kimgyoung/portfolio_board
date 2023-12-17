package com.example.demo.comment;

import com.example.demo.board.Board;
import com.example.demo.board.BoardRepository;
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
        // commentDto boardId를 이용하여 해당 게시글을 찾음
        Optional<Board> optionalBoard =
                boardRepository.findById(commentDto.getBoardId());
        // 게시글이 존재하면, 댓글 엔티티를 생성하고 저장, 반환
        if(optionalBoard.isPresent()){
            Board foundBoard  = optionalBoard.get();
            Comment comment = commentDto.toEntity(foundBoard);
            return commentRepository.save(comment);
        } else {
            return null;
        }
    }
    // 특정 게시글에 대한 댓글 목록을 조회
    public List<CommentDto> getCommentsByBoardId(Long boardId) {
        List<Comment> commentList = commentRepository.findByBoardId(boardId);
        //조회된 댓글 목록을 CommentDto로 변환하고 리스트로 반환
        return commentList.stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 댓글 삭제
    @Transactional
    public Comment deleteComment(Long commentId) {
        Comment deletedComment = commentRepository.findById(commentId).orElse(null);
        if (deletedComment != null) {
            commentRepository.deleteById(commentId);
        }
        return deletedComment;
    }

    // 특정 댓글 업데이트
    @Transactional
    public Comment updateComment(Long commentId, String contents) {
        // commentId를 이용하여 업데이트할 댓글을 조회
        Optional<Comment> optionalComment =
                commentRepository.findById(commentId);
        // 조회된 댓글이 존재하면, 주어진 contents를 이용하여 댓글을 업데이트
        if(optionalComment.isPresent()){
            Comment comment = optionalComment.get();
            CommentDto commentDto = new CommentDto(contents);
            comment.updateComment(commentDto);
            // 업데이트된 댓글 반환
            return comment;
        }
        return null;
    }
}

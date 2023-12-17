package com.example.demo.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    // 댓글을 저장
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDto commentDto){
        // 전달 받은 commentDto에 댓글을 저장, 저장된 댓글 정보를 SavedCommentDto로 변환
        CommentDto SavedCommentDto = CommentDto.fromEntity(commentService.save(commentDto));
        if (SavedCommentDto != null){
            return new ResponseEntity<>(SavedCommentDto, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // 특정 게시글에 대한 댓글 목록을 조회
    @GetMapping("/list/{boardId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long boardId) {
        // 주어진 boardId에 해당하는 게시글의 댓글 목록을 조회
        List<CommentDto> commentList = commentService.getCommentsByBoardId(boardId);
        // 댓글 목록과 서버 ok 응답
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }
    // 특정 댓글을 삭제
    @GetMapping("/delete/{commentId}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long commentId) {
        // 주어진 commentId에 해당하는 댓글을 삭제, 삭제된 댓글 정보를 CommentDto로 변환
        Comment deletedComment = commentService.deleteComment(commentId);
        CommentDto deletedCommentDto = CommentDto.fromEntity(deletedComment);
        return ResponseEntity.ok(deletedCommentDto);
    }

    // 특정 댓글을 업데이트
    @PostMapping("/update/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId, @RequestParam String contents){
        // 주어진 commentId와 contents를 이용하여 댓글을 업데이트
        Comment updatedComment = commentService.updateComment(commentId, contents);
        if (updatedComment != null) {
            // 업데이트된 댓글 정보를 CommentDto로 변환
            CommentDto updatedCommentDto = CommentDto.fromEntity(updatedComment);
            return ResponseEntity.ok(updatedCommentDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

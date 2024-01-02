package com.example.demo.comment;

import com.example.demo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity save(@ModelAttribute CommentDto commentDto,
                               @AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long userId = userDetails.getUser().getId();
        commentDto.setUserId(userId);
        // 전달 받은 commentDto에 댓글을 저장, 저장된 댓글 정보를 SavedCommentDto로 변환
        CommentDto SavedCommentDto = CommentDto.fromEntity(commentService.save(commentDto));
        return new ResponseEntity<>(SavedCommentDto, HttpStatus.OK);
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
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long commentId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            // 로그인 되지 않은 사용자에 대한 처리
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long userId = userDetails.getUser().getId();

        // 주어진 commentId에 해당하는 댓글을 삭제, 삭제된 댓글 정보를 CommentDto로 변환
        try {
            Comment deletedComment = commentService.deleteComment(commentId, userId);
            CommentDto deletedCommentDto = CommentDto.fromEntity(deletedComment);
            return ResponseEntity.ok(deletedCommentDto);
        } catch (IllegalArgumentException e) {
            // 댓글 삭제 권한이 없는 사용자에 대한 처리
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // 특정 댓글을 업데이트
    @PostMapping("/update/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId,
                                                    @RequestParam String contents,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        // 주어진 commentId와 contents, userId 이용 하여 댓글을 업데이트
        Long userId = userDetails.getUser().getId();
        Comment updatedComment = commentService.updateComment(commentId, contents, userId);

        if (updatedComment != null) {
            // 업데이트된 댓글 정보를 CommentDto로 변환
            CommentDto updatedCommentDto = CommentDto.fromEntity(updatedComment);
            return ResponseEntity.ok(updatedCommentDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

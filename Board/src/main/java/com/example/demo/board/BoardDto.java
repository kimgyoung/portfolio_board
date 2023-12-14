package com.example.demo.board;

import com.example.demo.file.FileDto;
import com.example.demo.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 복사 생성자
@ToString
public class BoardDto {

    // username -> User 클래스 생성? user id? nickname?
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean fileExists;
    private List<FileDto> boardFiles;
    private Long userId;

    public Board toEntity(User user){
        return Board.builder()
                .id(id)
                .title(title)
                .username(username)
                .contents(contents)
                .createTime(createTime)
                .updateTime(LocalDateTime.now())
                .fileExists(fileExists)
                .user(user)
                .build();
    }

    public static BoardDto toBoardDto(Board board){
        List<FileDto> fileDtos = board.getBoardFiles().stream()
                .map(FileDto::toFileDto)
                .collect(Collectors.toList());
        User user = board.getUser();
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getUsername(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime(),
                board.getFileExists(),
                fileDtos,  // Add 'fileDtos' to constructor
                user.getId()
        );
    }
}
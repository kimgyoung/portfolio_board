package com.example.demo.dto;

import com.example.demo.entity.Board;
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

    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean fileExists;
    private List<FileDto> boardFiles;

    public Board toEntity(){
        return Board.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .username(username)
                .createTime(createTime)
                .updateTime(LocalDateTime.now())
                .fileExists(fileExists)
                .build();
    }

    public static BoardDto toBoardDto(Board board){
        List<FileDto> fileDtos = board.getBoardFiles().stream()
                .map(FileDto::toFileDto)
                .collect(Collectors.toList());

        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getUsername(),
                board.getCreateTime(),
                board.getUpdateTime(),
                board.getFileExists(),
                fileDtos  // Add 'fileDtos' to constructor
        );
    }
}



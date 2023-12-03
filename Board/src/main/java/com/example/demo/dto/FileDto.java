package com.example.demo.dto;

import com.example.demo.entity.Board;
import com.example.demo.entity.BoardFile;
import lombok.*;

@AllArgsConstructor // toFileDto 때문에 설정
@ToString
@Setter
@Getter
@NoArgsConstructor
public class FileDto {

    private String filePath;

    private String fileName;

    private String fileType;

    private Long fileSize;

    // 게시판의 키(pk)를 받아 와야 하니까 Dto에만 보드 아이디 존재
    private Long boardId;

    // uuid(랜덤키)
    private String uuid;

    public BoardFile toEntity(Board board){
        return BoardFile.builder()
                .filePath(filePath)
                .fileName(fileName)
                .fileType(fileType)
                .fileSize(fileSize)
                .uuid(uuid)
                .board(board)
                .build();
    }
    public static FileDto toFileDto(BoardFile boardFile){
        return new FileDto(
                boardFile.getFilePath(),
                boardFile.getFileName(),
                boardFile.getFileType(),
                boardFile.getFileSize(),
                boardFile.getBoard().getId(),
                boardFile.getUuid()
        );
    }


}

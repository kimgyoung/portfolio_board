package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Setter
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 파일 경로 - 필요 낫널, 길이 설정은 X
    private String filePath;

    // 파일 이름 - 필요 낫널
    private String fileName;

    // 파일 포맷 - 필요 낫널
    private String fileType;

    // 파일 크기 - ?
    private Long fileSize;

    // uuid(랜덤키) - 결국 에는 저장이 되어야 하니까
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardFile(Long id, String filePath, String fileName, String fileType, Long fileSize, Board board, String uuid) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uuid = uuid;
        this.board = board;
    }
}

package com.example.demo.user;

import com.example.demo.board.Board;
import com.example.demo.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> Comments = new ArrayList<>();

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Convert(converter = StringArrayConverter.class)
    private List<String> roles = new ArrayList<>();

    @Column (length = 45, nullable = false)
    private String nickname;

    @Builder
    public User(Long id, String email, String password, List<String> roles, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.nickname = nickname;
    }
}

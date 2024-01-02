package com.example.demo.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;
public class UserRequest {
    @Setter
    @Getter
    public static class JoinDto{
        // 데이터가 비어 있을 수 없는 상태.
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "8자 이상 20자 이내로 작성 가능합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야 하고 공백이 포함될 수 없습니다.")
        private String password;

        @NotEmpty
        @Size(min = 2, max = 10, message = "2자 이상 10자 이내로 작성 가능합니다")
        private String nickname;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }
    }
    @Setter
    @Getter
    public static class LoginDto{
        // 데이터가 비어 있을 수 없는 상태.
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "8자 이상 20자 이내로 작성 가능합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야 하고 공백이 포함될 수 없습니다.")
        private String password;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }
    }
}

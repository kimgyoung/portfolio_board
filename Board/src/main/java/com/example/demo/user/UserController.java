package com.example.demo.user;

import com.example.demo.error.exception.Exception401;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserDto userDto){
        userService.join(userDto);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDto userDto){
        try {
            String jwt = userService.login(userDto);
            return ResponseEntity.ok().header("Authorization", "Bearer " + jwt).body(ApiUtils.success(null));
        } catch (Exception e) {
            throw new Exception401("인증 실패");
        }
    }
}

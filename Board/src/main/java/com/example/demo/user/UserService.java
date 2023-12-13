package com.example.demo.user;

import com.example.demo.error.exception.Exception400;
import com.example.demo.error.exception.Exception401;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void join(UserDto userDto) {
        // 동일한 이메일 존재 하는 지 확인
        Optional<User> byEmail = userRepository.findByEmail(userDto.getEmail());
        if(byEmail.isPresent()){
            throw new Exception400("이미 존재 하는 이메일 입니다." + userDto.getEmail());
        }
        // password 인코딩
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword); // 인코딩 된 비밀번호를 UserDto에 설정
        userRepository.save(userDto.toEntity());
    }

    @Transactional
    public String login(UserDto userDto) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());
            // anonymousUser = 비인증
            Authentication authentication = authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken
            );
            // ** 인증 완료 값을 받아 온다.
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            // ** 토큰 발급.
            return JwtTokenProvider.create(customUserDetails.getUser());

        } catch (Exception e) {
            throw new Exception401("인증 되지 않음.");
        }
    }
}

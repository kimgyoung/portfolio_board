package com.example.demo.user;

import com.example.demo.error.exception.Exception400;
import com.example.demo.error.exception.Exception401;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public void join(UserRequest.JoinDto joinDto) {
        // 동일한 이메일 존재 하는 지 확인
        Optional<User> byEmail = userRepository.findByEmail(joinDto.getEmail());
        if(byEmail.isPresent()){
            throw new Exception400("이미 존재 하는 이메일 입니다." + joinDto.getEmail());
        }
        // password 인코딩
        String encodedPassword = passwordEncoder.encode(joinDto.getPassword());
        joinDto.setPassword(encodedPassword); // 인코딩 된 비밀번호를 UserDto에 설정
        userRepository.save(joinDto.toEntity());
    }

    @Transactional
    public String login(UserRequest.LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
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

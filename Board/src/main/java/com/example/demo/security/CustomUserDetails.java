package com.example.demo.security;

import com.example.demo.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    // 여기 유저 정보가 저장 되고 있음
    private final User user;

    @Override
    // ** 사용자에게 부여된 권한을 GrantedAuthority 객체의 컬렉션으로 반환.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    // ** 저장된 사용자의 비밀번호를 반환
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    // ** 저장된 사용자의 이메일(사용자 이름으로 사용)을 반환.
    public String getUsername() {
        return this.user.getEmail();
    }

    // ** 이 예제들은 단순화를 위해 항상 true를 반환
    @Override
    // ** 사용자 계정이 만료되지 않았는지 여부를 반환.
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    // ** 사용자 계정이 잠기지 않았는지 여부를 반환.
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // ** 사용자의 인증 정보가 만료되지 않았는지 여부를 반환.
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // ** 사용자 계정이 활성화되어 있는지 여부를 반환.
    public boolean isEnabled() {
        return true;
    }
}

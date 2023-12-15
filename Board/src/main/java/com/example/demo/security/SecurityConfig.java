package com.example.demo.security;

import com.example.demo.error.exception.Exception401;
import com.example.demo.error.exception.Exception403;
import com.example.demo.util.FilterResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@RequiredArgsConstructor
@Configuration // ** 현재 클래스를 (설정 클래스)로 설정
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
        //* BCrypt : 기본으로 사용. 가장 많이 사용 되는 알고리즘
        //* SCrypt : 개발자가 직접 필요에 따라 변경 가능
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity>{
        @Override
        public void configure(HttpSecurity httpSecurity) throws Exception {
            AuthenticationManager authenticationManager = httpSecurity.getSharedObject(
                    AuthenticationManager.class
            );
            httpSecurity.addFilter(new JwtAuthenticationFilter(authenticationManager));
            super.configure(httpSecurity);
        }
    }

    @Bean // 스프링 빈으로 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf().disable(); // postman 접근 해야 함 - CSR 할때

        // 2. iframe 거부 설정
        http.headers().frameOptions().sameOrigin();

        // 3. cors 재설정
        http.cors().configurationSource(configurationSource());

        // 4. jSessionId 사용 거부
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 정책

        // 5. form 로긴 해제 (UsernamePasswordAuthenticationFilter 비활성화) (폼 로그인 비활성화)
        http.formLogin().disable();

        // 6. 로그인 인증창이 뜨지 않게 비활성화(기본 인증 비활성화)
        http.httpBasic().disable();

        // 7. 커스텀 필터 적용 (시큐리티 필터 교환) 커스텀 필터 적용
        http.apply(new CustomSecurityFilterManager());

        // 8. 인증 실패 처리
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            log.warn("인증되지 않은 사용자가 자원에 접근하려 합니다 : " + authException.getMessage());
            FilterResponseUtils.unAuthorized(response, new Exception401("인증되지 않았습니다"));
        });

        // 9. 권한 실패 처리
        http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
            log.warn("권한이 없는 사용자가 자원에 접근하려 합니다 : " + accessDeniedException.getMessage());
            FilterResponseUtils.forbidden(response, new Exception403("권한이 없습니다"));
        });

        // 10. 인증, 권한 필터 설정
        http.authorizeRequests(
                authorize -> {
                    try {
                        authorize.antMatchers("/carts/**","/options/**", "/orders/**").authenticated()
                                .antMatchers("/admin/**")
                                .hasAnyRole("ADMIN", "USER")
                                .anyRequest().permitAll()
                                .and()
                                .exceptionHandling()
                                .accessDeniedPage("/");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return http.build();
    }

    // ** 규칙: 헤더(Authorization), 메서드, IP 주소, 클라이언트의 쿠키 요청을 허용
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfigurationSource = new CorsConfiguration();
        corsConfigurationSource.addAllowedHeader("*"); // 모든 헤더를 허용
        corsConfigurationSource.addAllowedMethod("*"); // GET, POST, PUT, DELETE 등의 모든 메서드를 허용
        corsConfigurationSource.addAllowedOriginPattern("*"); // 모든 IP주소를 허용
        corsConfigurationSource.setAllowCredentials(true); // 클라이언트 쿠키 요청 허용
        corsConfigurationSource.addExposedHeader("Authorization"); // 헤더

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource
                = new UrlBasedCorsConfigurationSource();

        // ** (/) 들어 오는 모든 유형의 URL 패턴을 허용.
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfigurationSource);
        return urlBasedCorsConfigurationSource;
    }
}
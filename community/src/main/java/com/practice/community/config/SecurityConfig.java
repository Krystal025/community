package com.practice.community.config;

import com.practice.community.filter.LoginFilter;
import com.practice.community.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Spring 설정 클래스로 지정
@EnableWebSecurity // Spring Security 설정을 활성화함 (사용자 정의 보안설정을 구성하기 위함)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // 비밀번호를 단방향으로 암호화하는 PasswordEncoder 구현체
    }

    @Bean // 메서드의 반환값을 스프링 빈 객체로 등록
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http
                // 세션을 stateless로 관리하기 때문에 csrf 공격이 존재하지 않으므로 csrf 보안 비활성화
                .csrf((auth)-> auth.disable())
                // 폼 로그인 방식 대신 API 기반의 인증방식을 사용하기 때문에 비활성화
                .formLogin((auth)-> auth.disable())
                // 토큰 기반 인증을 사용하기 때문에 기본 인증을 통해 검증하지 않도록 비활성화
                .httpBasic((auth)-> auth.disable())
                // 요청경로별 인가 작업
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers("/", "/login", "/auth/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                // 필터를 Bean으로 등록
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                // 세션 정책을 Stateless로 설정하여 서버가 세션을 생성하거나 저장하지 않도록 함
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}

package com.practice.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Spring 설정 클래스로 지정
@EnableWebSecurity // Spring Security 설정을 활성화함 (사용자 정의 보안설정을 구성하기 위함)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){ // 비밀번호를 단방향으로 암호화하는 PasswordEncoder 구현체
        return new BCryptPasswordEncoder();
    }

    @Bean // 메서드의 반환값을 스프링 빈 객체로 등록
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((auth) -> auth // 특정 요청 경로에 대한 인가 작업
                        .requestMatchers("/", "/login", "/loginProc", "signup", "signupProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN") // 특정 역할을 가진 사용자만 접근 가능
                        .requestMatchers("/myPage/**").hasAnyRole("ADMIN", "USER") // ** : 와일드카드
                        .anyRequest().authenticated() // 위에서 지정하지 않은 다른 요청에 대한 권한 설정 (로그인한 사용자만 접근)
                );
        http
                .formLogin((auth)-> auth.loginPage("/login") // 사용자 정의 로그인 페이지 (로그인이 필요할 때 이 페이지로 리다이렉트)
                        .loginProcessingUrl("/loginProc") // 로그인 요청을 처리할 URL
                        .permitAll() // 로그인 페이지와 로그인 처리 URL을 모든 사용자에게 열어줌
                );
        http
                .csrf((auth)-> auth.disable());

        return http.build();
    }

}

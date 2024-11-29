package com.practice.community.config;

import com.practice.community.filter.JwtAuthorizationFilter;
import com.practice.community.filter.LoginAuthenticationFilter;
import com.practice.community.filter.OAuth2AuthorizationFilter;
import com.practice.community.user.service.CustomOAuth2UserService;
import com.practice.community.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration // Spring 설정 클래스로 지정
@EnableWebSecurity // Spring Security 설정을 활성화함 (사용자 정의 보안설정을 구성하기 위함)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler OAuth2SuccessHandler;

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
                .cors((cors) -> cors // CORS 설정 활성화
                        .configurationSource(request -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 허용할 도메인
                            configuration.setAllowedMethods(Collections.singletonList("*")); // 모든 HTTP 메서드 혀용
                            configuration.setAllowCredentials(true); // 쿠키 전송 허용
                            configuration.setAllowedHeaders(Collections.singletonList("*")); // 모든 요청 헤더 허용
                            configuration.setMaxAge(3600L); // CORS 요청에 대한 캐시 유효시간 설정 (1시간)
                            configuration.setExposedHeaders(Collections.singletonList("Set-Cookie")); // 서버가 응답헤더에 포함시킨 Set-Cookie를 클라이언트가 접근할 수 있도록 설정
                            configuration.setExposedHeaders(Collections.singletonList("Authorization")); // 클라이언트가 접근가능한 응답 헤더
                            return configuration;
                        }));
        http
                // 세션을 stateless로 관리하기 때문에 csrf 공격이 존재하지 않으므로 csrf 보안 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 폼 로그인 방식 대신 API 기반의 인증방식을 사용하기 때문에 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 토큰 기반 인증을 사용하기 때문에 기본 인증을 통해 검증하지 않도록 비활성화
                .httpBasic(AbstractHttpConfigurer::disable);
        http
                // OAuth2 기반 로그인 활성화
                .oauth2Login((oauth2) -> oauth2
                        // 제공자로부터 Access 토큰을 받은 후 사용자 정보(e.g. 이메일, 이름)를 가져오기 위한 엔드포인트 설정
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                // 사용자 정보를 처리할 서비스 설정
                                .userService(customOAuth2UserService))
                        // OAuth 인증이 성공적으로 완료된 후 실행될 성공 핸들러 설정
                        .successHandler(OAuth2SuccessHandler));
        http
                // 요청경로별 인가 작업
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers("/", "/user/signup", "/login", "/oauth2/callback", "/home", "/board/list").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());
        http
                // 소셜 로그인 인증 핉터
                .addFilterBefore(new OAuth2AuthorizationFilter(jwtTokenProvider), LoginAuthenticationFilter.class)
                // 일반 로그인 인증 필터 (최초 로그인 또는 토큰 만료시 이메일과 비밀번호로 인증 처리)
                .addFilterBefore(new LoginAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                // 모든 요청에 대해 JWT 토큰이 유효한지 검증 (이미 로그인을 해서 토큰을 가지고 있을 때 해당 토큰의 유효성을 검사)
                .addFilterAfter(new JwtAuthorizationFilter(jwtTokenProvider), LoginAuthenticationFilter.class)
                // 세션 정책을 Stateless로 설정하여 서버가 세션을 생성하거나 저장하지 않도록 함
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}

package com.practice.community.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.community.user.dto.CustomUserDetails;
import com.practice.community.user.dto.LoginRequestDto;
import com.practice.community.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// 로그인 요청 (POST 요청)에 대해서만 인증을 처리하는 필터로 폼 로그인 방식에서 사용됨
//@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl("/login");  // /login 경로에 대해 필터가 동작하도록 설정
    }

    // 로그인 시도시 인증을 처리하는 메소드 (HTTP 요청에서 username과 password를 추출하여 인증 토큰 생성 후 인증 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // JSON 형식의 요청을 파싱하여 `LoginRequestDto`로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid login request format");
        }

        System.out.println("username: " + loginRequestDto.getUsername() + "password: " + loginRequestDto.getPassword());

        // 토큰 생성 및 인증 시도
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // UsernamePasswordAuthenticationToken 인증
        Authentication authentication = authenticationManager.authenticate(authToken);
        System.out.println("Authentication successful: " + authentication);
        return authentication;
    }

    // 인증 성공시 호출되는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 인증된 사용자 정보 반환
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        // 디버깅을 위한 로그 추가
        System.out.println("Authentication Successful");

        // 사용자 이메일 추출
        String userEmail = customUserDetails.getUsername();
        // 사용자 역할을 컬렉션에 담고, iterator로 반복하여 추출
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String userRole = auth.getAuthority();
        // JWT 토큰 생성 (만료시간 : 10시간)
        String token = jwtTokenProvider.createJwt(userEmail, userRole, 60*60*10L);

        // 디버깅을 위한 로그 추가
        System.out.println("Generated Token: " + token);

        // Authorization 필드에 Bearer 접두사를 붙여 토큰을 포함시킨 뒤 HTTP 응답 헤더에 추가
        response.addHeader("Authorization", "Bearer " + token);
    }

    // 인증 실패시 호출되는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }

}

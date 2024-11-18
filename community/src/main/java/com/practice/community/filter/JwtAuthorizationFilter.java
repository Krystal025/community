package com.practice.community.filter;

import com.practice.community.user.dto.CustomUserDetails;
import com.practice.community.user.enums.Role;
import com.practice.community.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        // JWT가 없을 경우
        if(authorization == null || !authorization.startsWith("Bearer ")){
            System.out.println("Token Null");
            filterChain.doFilter(request, response);
            return;
        }
        // Bearer 접두사 분리 (공백으로 분리한 뒤 인덱스[1] 요소 선택)
        String token = authorization.split(" ")[1];
        // JWT 만료 여부 확인
        if(jwtTokenProvider.isExpired(token)){
            System.out.println("Token Expired");
            filterChain.doFilter(request, response);
            return;
        }
        // 유효한 토큰일 경우 해당 토큰에서 사용자 정보 추출
        String userEmail = jwtTokenProvider.getUserEmail(token);
        String userRole = jwtTokenProvider.getUserRole(token);
        // 인증에 필요한 사용자 정보 객체 생성
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userEmail(userEmail) // JWT 토큰에서 추출한 userEmail
                .password(null) // 비밀번호는 어차피 사용하지 않으므로 임시값 사용
                .role(Role.valueOf(userRole)) // JWT 토큰에서 추출한 userRole
                .build();
        // 인증된 사용자 정보로 로그인용 토큰 생성 (로그인 후 JWT에서 추출된 사용자 정보 사용)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        // 인증 정보를 SecurityContext라는 메모리에 저장하여 인증상태를 유지함
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}

package com.practice.community.filter;

import com.practice.community.user.dto.CustomOAuth2User;
import com.practice.community.user.dto.OAuth2Info;
import com.practice.community.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2AuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Cookie에서 Authorization에 있는 토큰 추출
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {
                token = cookie.getValue(); // Authorization 쿠키에서 JWT 토큰 추출
            }
        }
        if(token == null){
            System.out.println("Token Null");
            filterChain.doFilter(request, response);
            return;
        }
        if(jwtTokenProvider.isExpired(token)){
            System.out.println("Token Expired");
            filterChain.doFilter(request, response);
            return;
        }
        // 유효한 토큰일 경우 토큰에서 사용자 정보 추출
        String socialId = jwtTokenProvider.getSocialId(token);
        String email = jwtTokenProvider.getEmail(token);
        String role = jwtTokenProvider.getRole(token);
        // 사용자 정보를 OAuth2InfoDto 객체로 생성
        OAuth2Info oAuth2Info = OAuth2Info.builder()
                .socialId(socialId)
                .email(email)
                .name(email)
                .role(role)
                .build();
        // 사용자 정보로 CustomOAuth2User 객체 생성 (OAuth2User)
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2Info);
        // UsernamePasswordAuthenticationToken 생성하여 인증 객체로 사용 (역할/권한 포함)
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        // 인증 정보를 SecurityContext에 저장하여 후속 필터에서 인증 정보를 사용할 수 있게 함
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}

package com.practice.community.filter;

import com.practice.community.user.dto.CustomOAuth2User;
import com.practice.community.user.dto.OAuth2Info;
import com.practice.community.user.enums.Role;
import com.practice.community.util.CookieUtils;
import com.practice.community.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
        String accessToken = request.getHeader("Authorization");
        // 토큰 존재여부 확인
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            System.out.println("Access_Token is Null");
            filterChain.doFilter(request, response);
            return;
        }
        accessToken = accessToken.substring(7);
        // 토큰 만료여부 확인
        if(jwtTokenProvider.isExpired(accessToken)){
            System.out.println("Access_Token is Expired");
            String refreshToken = CookieUtils.getCookie(request, "Refresh_Token");
            if(refreshToken != null && !jwtTokenProvider.isExpired(refreshToken)) {
                String newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken);
                response.setHeader("Authorization", "Bearer " + newAccessToken);
            }
            // 만료된 토큰을 갱신했을 경우 이전의 만료된 토큰에서 사용자 정보를 추출할 필요가 없기 때문에 return 시킴
            filterChain.doFilter(request, response);
            return;
        }
        // 유효한 토큰일 경우 토큰에서 사용자 정보 추출
        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        String userRole = jwtTokenProvider.getUserRole(accessToken);
        String socialId = jwtTokenProvider.getSocialId(accessToken);
        // 사용자 정보를 OAuth2InfoDto 객체로 생성
        OAuth2Info oAuth2Info = OAuth2Info.builder()
                .userEmail(userEmail)
                .userName(userEmail)
                .userRole(Role.valueOf(userRole))
                .socialId(socialId)
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

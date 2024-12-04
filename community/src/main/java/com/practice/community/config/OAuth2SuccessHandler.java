package com.practice.community.config;

import com.practice.community.user.dto.CustomOAuth2User;
import com.practice.community.user.dto.OAuth2Info;
import com.practice.community.user.dto.OAuth2RequestDto;
import com.practice.community.util.CookieUtils;
import com.practice.community.util.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2 인증을 거친 사용자 정보를 CustomOAuth2User 객체로 반환
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        // OAuth 인증을 통해 얻은 소셜 ID
        String socialId = customOAuth2User.getSocialId();
        // OAuth 인증을 통해 얻은 소셜 로그인 사용자 식별용 이메일
        String email = customOAuth2User.getEmail();
        // 인증된 사용자의 권한정보 목록
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 권한 목록에서 첫번째 권한을 추출함
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        // 소셜 ID와 역할을 이용하여 JWT 토큰을 생성함 (토큰 만료시간 : 1시간)
        String accessToken = jwtTokenProvider.createOAuthJwt(socialId, email, role);
        String refreshToken = jwtTokenProvider.createRefreshJwt(customOAuth2User.getUserId(), "social");
        // 생성된 JWT 토큰을 헤더에 담아 클라이언트에 전달함
        response.addHeader("Authorization", "Bearer " + accessToken);
        CookieUtils.addCookie(response, refreshToken);
        // 디버깅을 위한 로그 추가
        System.out.println("Generated AccessToken: " + "Bearer " + accessToken);
        System.out.println("Generated RefreshToken: " + refreshToken);
        // 클라이언트가 받은 토큰을 기반으로 리다이렉트 시킴 (로그인 후 사용자가 이동할 페이지)
        response.sendRedirect("http://localhost:8080/home");
    }

}

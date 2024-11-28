package com.practice.community.config;

import com.practice.community.user.dto.CustomOAuth2User;
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
        // OAuth 인증을 통해 얻은 소셜 ID를 가져옴
        String socialId = customOAuth2User.getSocialId();
        // 인증된 사용자의 권한정보를 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 권한 목록에서 첫번째 권한을 추출함
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        // 소셜 ID와 역할을 이용하여 JWT 토큰을 생성함 (토큰 만료시간 : 1시간)
        String token = jwtTokenProvider.createJwtForOAuth(socialId, role, 3600000L);
        // 생성된 JWT 토큰을 쿠키에 담아 클라이언트에 전달함
        response.addCookie(createCookie("Authorization", token));
        // 클라이언트가 받은 토큰을 기반으로 리다이렉트 시킴 (로그인 후 사용자가 이동할 페이지)
        response.sendRedirect("http://localhost:3000/");
    }

    private Cookie createCookie(String key, String value) {
        // 쿠키 객체 생성 (key : "Authorization", value : JWT 토큰값)
        Cookie cookie = new Cookie(key, value);
        // 쿠키 최대 유효시간
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true); // HTTPS 환경에서만 실행가능하도록 설정
        // 쿠키가 서버의 루트 경로에서만 유효하도록 설정
        cookie.setPath("/");
        // JavaScript에서 이 Cookie에 접근하지 못하도록 설정 (사용자의 쿠키 수정 및 탈취 방지)
        cookie.setHttpOnly(true);
        return cookie;
    }
}

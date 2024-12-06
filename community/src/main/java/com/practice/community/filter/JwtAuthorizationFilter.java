package com.practice.community.filter;

import com.practice.community.user.dto.CustomUserDetails;
import com.practice.community.user.enums.Role;
import com.practice.community.util.CookieUtils;
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
        // 헤더에서 Authorization에 있는 토큰 추출
        String accessToken = request.getHeader("Authorization");
        // 토큰이 없거나 "Bearer "로 시작하지 않으면 바로 필터 체인 진행
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            System.out.println("Access Token is Null");
            filterChain.doFilter(request, response);
            return;
        }
        // "Bearer " 부분을 잘라내고 실제 토큰만 추출
        accessToken = accessToken.substring(7);
        // 소셜 로그인 사용자는 이 필터가 적용되지 않도록 함
        String authType = jwtTokenProvider.getAuthType(accessToken);
        if ("social".equals(authType)) {
            filterChain.doFilter(request, response);
            return;
        }
        // JWT Access 토큰 만료 여부 확인
        if (jwtTokenProvider.isExpired(accessToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Access Token expired");
            System.out.println("Access Token Expired");
            return;
        }
//                // Access 토큰 만료시 쿠키에서 Refresh 토큰을 가져옴
//                String refreshToken = CookieUtils.getCookie(request, "Refresh_Token");
//                // Refresh 토큰이 유효한지 확인
//                if(refreshToken != null && !jwtTokenProvider.isExpired(refreshToken)){
//                    // String newAccessToken = jwtTokenProvider.createJwt();
//                    String newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken);
//                    response.setHeader("Authorization", "Bearer " + newAccessToken);
//                    System.out.println("Bearer " + newAccessToken);
//                }
//                filterChain.doFilter(request, response);
//                return;
        // 유효한 토큰일 경우 해당 토큰에서 사용자 정보 추출
        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        String userRole = jwtTokenProvider.getUserRole(accessToken);
        // 인증에 필요한 사용자 정보 객체 생성
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userEmail(userEmail) // JWT 토큰에서 추출한 userEmail
                .userPwd(null) // 비밀번호는 어차피 사용하지 않으므로 임시값 사용
                .userRole(Role.valueOf(userRole)) // JWT 토큰에서 추출한 userRole
                .build();// 인증된 사용자 정보로 로그인용 토큰 생성 (로그인 후 JWT에서 추출된 사용자 정보 사용)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities());// 인증 정보를 SecurityContext라는 메모리에 저장하여 인증상태를 유지함
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}

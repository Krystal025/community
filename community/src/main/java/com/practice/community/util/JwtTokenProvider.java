package com.practice.community.util;

import com.practice.community.exception.ErrorCode;
import com.practice.community.exception.custom.UserNotFoundException;
import com.practice.community.user.entity.User;
import com.practice.community.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // JWT 암호화 알고리즘에 사용될 비밀키 저장
    private final SecretKey secretKey;
    private final UserRepository userRepository;

    // JWT에 사용할 암호화 키 설정 (yml에 설정된 환경변수를 가져와 JWT에서 요구하는 형태로 변환하여 사용)
    public JwtTokenProvider(@Value("${jwt.secret}")String secret, UserRepository userRepository){
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.userRepository = userRepository;
    }

    // JWT 토큰에서 사용자 이메일 추출
    public String getUserEmail(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userEmail", String.class);
    }

    // JWT 토큰에서 사용자 역할 추출
    public String getUserRole(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userRole", String.class);
    }

    // JWT 토큰에서 소셜 ID 추출
    public String getSocialId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("socialId", String.class);
    }

    // JWT 토큰에서 로그인 방식 추출
    public String getAuthType(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("authType", String.class);
    }

    // JWT 토큰의 유효기간 추출 및 현재 시간과의 비교를 통한 만료 여부 확인
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // JWT 토큰 생성
    public String createAccessJwt(String userEmail, String userRole, String socialId){
        return Jwts.builder()
                .claim("userEmail", userEmail)
                .claim("userRole", userRole)
                .claim("socialId", socialId)
                .claim("authType", (socialId == null ? "basic" : "social"))
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행시점 설정
                .expiration(new Date(System.currentTimeMillis() + 2 * 60 * 1000L)) // 토큰 만료시점 설정
                .signWith(secretKey) // JWT 서명(signature)시 사용할 암호키 지정
                .compact(); // JWT의 Header, Payload, Signature를 결합하여 문자열로 반환
    }

    public String createRefreshJwt(Long userId, String authType){
        return Jwts.builder()
                .claim("userId", userId)
                .claim("authType", authType)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L)) // Refresh 토큰 유효기간 : 30일
                .signWith(secretKey)
                .compact();
    }

    public String  refreshAccessToken(String refreshToken){
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        Long userId = claims.get("userId", Long.class);

        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        String userEmail = user.getUserEmail();
        String userRole = user.getUserRole().name();
        String socialId = user.getSocialId();

        return createAccessJwt(userEmail, userRole, socialId);
    }

}

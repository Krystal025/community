package com.practice.community.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // JWT 암호화 알고리즘에 사용될 비밀키 저장
    private SecretKey secretKey;

    // JWT에 사용할 암호화 키 설정 (yml에 설정된 환경변수를 가져와 JWT에서 요구하는 형태로 변환하여 사용)
    public JwtTokenProvider(@Value("${jwt.secret}")String secret){
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT 토큰에서 사용자 이메일 추출
    public String getUserEmail(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userEmail", String.class);
    }

    // JWT 토큰에서 사용자 역할 추출
    public String getUserRole(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userRole", String.class);
    }

    // JWT 토큰의 유효기간 추출 및 현재 시간과의 비교를 통한 만료 여부 확인
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // JWT 토큰 생성
    public String createJwt(String userEmail, String userRole, Long expireTime){
        return Jwts.builder()
                .claim("userEmail", userEmail) // 사용자 이메일을 claim(key-value 구조)으로 추가
                .claim("userRole", userRole) // 사용자 역할을 claim으로 추가
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행시점 설정
                .expiration(new Date(System.currentTimeMillis() + expireTime)) // 토큰 만료시점 설정
                .signWith(secretKey) // JWT 서명(signature)시 사용할 암호키 지정
                .compact(); // JWT의 Header, Payload, Signature를 결합하여 문자열로 반환
    }
}

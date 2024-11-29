package com.practice.community.user.dto;

import lombok.Builder;
import lombok.Getter;

// OAuth2Response가 받은 사용자 정보 원본(JSON)을 변환하여 담고있는 객체
@Getter
@Builder
public class OAuth2Info {
     private String socialId; // 소셜 제공자와 ID를 조합한 고유 식별자
     private String provider; // 소셜 제공자
     private String email; // 사용자 이메일
     private String name; // 사용자 이름
     private String role; // 사용자 권한
}
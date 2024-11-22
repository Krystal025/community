package com.practice.community.user.dto;

import lombok.Builder;
import lombok.Getter;

// OAuth2로부터 받은 사용자 정보를 전달하고, 애플리케이션 내부에서 활용하기 위한 객체
@Getter
@Builder
public class OAuth2InfoDto {
     private String socialUserId; // 애플리케이션에서 사용자 식별에 사용하는 고유한 사용자 이름
     private String name; // 사용자의 소셜 계정 이름
     private String role; // 사용자의 권한
}

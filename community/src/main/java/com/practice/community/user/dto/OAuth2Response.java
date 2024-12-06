package com.practice.community.user.dto;

// OAuth2 로그인에서 제공자의 응답데이터를 표준화하기 위한 인터페이스
public interface OAuth2Response {
    // 제공자
    String getProvider();
    // 제공자에서 발급해주는 아이디
    String getProviderId();
    // 이메일
    String getUserEmail();
    // 사용자 이름
    String getUserName();
}

package com.practice.community.user.dto;

import lombok.RequiredArgsConstructor;

import java.util.Map;

// Google에서 반환받은 사용자 정보를 OAuth2Response 형태로 변환
@RequiredArgsConstructor
public class GoogleResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "google"; // yml에 설정한 소셜로그인 제공자 ID
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getUserEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getUserName() {
        return attribute.get("name").toString();
    }
}

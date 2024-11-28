package com.practice.community.user.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// OAuth2Info에 담긴 데이터를 Spring Security 인증/인가 시스템이 요구하는 형태로 제공하는 객체
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2Info oAuth2Info;

    // 소셜 로그인에서 받은 원본 속성 데이터 반환 (인가 과정에서 사용됨)
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 사용자 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return oAuth2Info.getRole();
            }
        });
        return collection;
    }

    // 사용자 식별을 위한 이메일 반환
    @Override
    public String getName() {
        return oAuth2Info.getEmail();
    }

    // 소셜 로그인에서 제공하는 사용자 고유 ID 반환
    public String getSocialId(){
        return oAuth2Info.getSocialId();
    }
}

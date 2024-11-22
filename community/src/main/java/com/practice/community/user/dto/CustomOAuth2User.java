package com.practice.community.user.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// OAuth2 인증과정에서 받은 사용자 정보를 SpringSecurity 인증/인가 시스템이 요구하는 형태로 담고있는 객체
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2InfoDto oAuth2InfoDto;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return oAuth2InfoDto.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return oAuth2InfoDto.getName();
    }

    public String getSocialId(){
        return oAuth2InfoDto.getSocialUserId();
    }
}

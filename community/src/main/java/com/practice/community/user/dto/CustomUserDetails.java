package com.practice.community.user.dto;

import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Spring Security에서 인증/인가 작업에 필요한 정보를 제공하기 위해 사용자 정보를 담고있는 객체
@RequiredArgsConstructor //final 또는 @NotNull로 선언된 필드에 대해 자동으로 생성자를 만들어 줌
@Getter
@Builder
public class CustomUserDetails implements UserDetails {

    private final String userEmail;
    private final String password;
    private final Role role;

    // 사용자에게 부여된 권한(Role) 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    @Override
    public String getPassword() {
        return null;
    }

    // loadByUsername이 반환한 User 객체에서 사용자 식별자로 사용할 이메일을 반환
    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // User 엔티티로부터 CustomUserDetails 생성
    public static CustomUserDetails fromUser(User user) {
        return CustomUserDetails.builder()
                .userEmail(user.getUserEmail())
                .password(user.getUserPwd())
                .role(user.getUserRole())
                .build();
    }
}

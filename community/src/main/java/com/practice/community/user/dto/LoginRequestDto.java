package com.practice.community.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 요청시 사용자의 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}

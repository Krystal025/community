package com.practice.community.user.dto;

import com.practice.community.user.enums.Gender;
import com.practice.community.user.enums.Status;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 서버에서 클라이언트로 반환하는 사용자 정보
@Getter
@Builder
public class UserResponseDto {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userNickname;
    private Gender userGender;
    private LocalDate userBirthday;
    private Status userStatus;
    private LocalDateTime userCreatedAt;

}

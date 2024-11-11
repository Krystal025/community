package com.practice.community.user.dto;

import com.practice.community.user.enums.Gender;
import com.practice.community.user.enums.Status;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

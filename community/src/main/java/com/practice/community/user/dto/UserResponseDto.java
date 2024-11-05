package com.practice.community.user.dto;

import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Gender;
import com.practice.community.user.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userNickname;
    private Gender userGender;
    private LocalDate userBirthday;
    private Status userStatus;
    private LocalDateTime userCreatedAt;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.userNickname = user.getUserNickname();
        this.userGender = user.getUserGender();
        this.userBirthday = user.getUserBirthday();
        this.userStatus = user.getUserStatus();
        this.userCreatedAt = user.getUserCreatedAt();
    }
}

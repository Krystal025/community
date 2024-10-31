package com.practice.community.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userPwd;
    private String userNickname;
    private String userGender;
    private LocalDate userBirthday;
    private String userStatus;
    private LocalDateTime userCreatedAt;

}

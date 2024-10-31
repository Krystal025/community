package com.practice.community.user.dto;

import com.practice.community.user.entity.User;
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
    private User.Gender userGender;
    private LocalDate userBirthday;
    private User.Status userStatus;
    private LocalDateTime userCreatedAt;

    // User 엔티티를 매개변수로 받는 생성자
    public UserDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.userPwd = user.getUserPwd();
        this.userNickname = user.getUserNickname();
        this.userGender = user.getUserGender();
        this.userBirthday = user.getUserBirthday();
        this.userStatus = user.getUserStatus();
        this.userCreatedAt = user.getUserCreatedAt();
    }
}

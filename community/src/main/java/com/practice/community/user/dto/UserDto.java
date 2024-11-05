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
public class UserDto {

    private Long userId;

    @NotBlank(message = "Name must not be blank")
    private String userName;

    @NotBlank(message = "Email must not be blank")
    private String userEmail;

    @NotBlank(message = "Password must not be blank")
    private String userPwd;

    @NotBlank(message = "Nickname must not be blank")
    private String userNickname;

    private Gender userGender;

    @NotNull(message = "Birthday must not be blank")
    private LocalDate userBirthday;

    private Status userStatus;

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

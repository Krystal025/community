package com.practice.community.user.dto;

import com.practice.community.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// 회원가입 및 회원정보 수정시 사용자의 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Name must not be blank", groups = ValidationGroups.Create.class)
    private String userName;

    @NotBlank(message = "Email must not be blank", groups = ValidationGroups.Create.class)
    private String userEmail;

    @NotBlank(message = "Password must not be blank", groups = ValidationGroups.Create.class)
    private String userPwd;

    @NotBlank(message = "Nickname must not be blank", groups = ValidationGroups.Create.class)
    private String userNickname;

    private Gender userGender;

    private LocalDate userBirthday;

}

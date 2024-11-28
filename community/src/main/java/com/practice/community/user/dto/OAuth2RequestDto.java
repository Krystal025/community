package com.practice.community.user.dto;

import com.practice.community.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2RequestDto {

    @NotBlank(message = "Name must not be blank", groups = ValidationGroups.Create.class)
    private String userName;

    @NotBlank(message = "Email must not be blank", groups = ValidationGroups.Create.class)
    private String userEmail;

    //private String userPwd = null;

    private String userNickname;

    private Gender userGender;

    private LocalDate userBirthday;

}

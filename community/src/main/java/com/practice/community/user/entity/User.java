package com.practice.community.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(nullable = false, length = 100, unique = true)
    private String userEmail;

    @Column(nullable = false, length = 128)
    private String userPwd;

    @Column(nullable = false, length = 50, unique = true)
    private String userNickname;

    @Enumerated(EnumType.STRING)
    private Gender userGender = Gender.MALE;

    @Column(nullable = false)
    private LocalDate userBirthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status userStatus = Status.ACTIVE;

    @Column(updatable = false)
    private LocalDateTime userCreatedAt = LocalDateTime.now();

    public enum Gender{
        MALE, FEMALE
    }
    public enum Status{
        ACTIVE, INACTIVE
    }
    public User(String userName, String userEmail, String userPwd, String userNickname, Gender userGender, LocalDate userBirthday) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPwd = userPwd;
        this.userNickname = userNickname;
        this.userGender = userGender;
        this.userBirthday = userBirthday;
    }


}

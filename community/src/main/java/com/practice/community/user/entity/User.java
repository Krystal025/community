package com.practice.community.user.entity;

import com.practice.community.user.enums.Gender;
import com.practice.community.user.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // 기존 인스턴스를 복사하여 새로운 인스턴스를 만들고, 일부 필드만 변경할 수 있도록 함를
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
    private Gender userGender;

    @Column(nullable = false)
    private LocalDate userBirthday;

    @Enumerated(EnumType.STRING)
    private Status userStatus;

    @Column(updatable = false)
    private LocalDateTime userCreatedAt;

    @PrePersist // 처음 생성되는 엔티티가 DB에 저장되기 전에 호출됨
    private void onCreate() {
        if (this.userGender == null) {
            this.userGender = Gender.MALE; // 기본값 설정을 통해 무결성 보장
        }
        if (this.userStatus == null) {
            this.userStatus = Status.ACTIVE;
        }
        this.userCreatedAt = LocalDateTime.now();
    }

//    public enum Gender{
//        MALE, FEMALE
//    }
//
//    public enum Status{
//        ACTIVE, INACTIVE
//    }

//    public User(String userName, String userEmail, String userPwd, String userNickname, Gender userGender, LocalDate userBirthday) {
//        this.userName = userName;
//        this.userEmail = userEmail;
//        this.userPwd = userPwd;
//        this.userNickname = userNickname;
//        this.userGender = userGender;
//        this.userBirthday = userBirthday;
//    }

//    public void updateUser(String userEmail, String userPwd, String userNickname){
//        this.userEmail = userEmail;
//        this.userPwd = userPwd;
//        this.userNickname = userNickname;
//    }

//    public void deactivateUser() {
//        this.userStatus = Status.INACTIVE;
//    }

}

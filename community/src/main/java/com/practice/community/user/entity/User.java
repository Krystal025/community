package com.practice.community.user.entity;

import com.practice.community.user.enums.Gender;
import com.practice.community.user.enums.Role;
import com.practice.community.user.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
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

    @Column(length = 128)
    private String userPwd;

    @Column(nullable = false, length = 50, unique = true)
    private String userNickname;

    @Enumerated(EnumType.STRING)
    private Gender userGender;

    @Column
    private LocalDate userBirthday;

    @Enumerated(EnumType.STRING)
    private Status userStatus;

    @Column(updatable = false)
    private LocalDateTime userCreatedAt;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column(length = 100, unique = true)
    private String socialUserId;

    @PrePersist // 처음 생성되는 엔티티가 DB에 저장되기 전에 호출됨
    private void onCreate() {
        if (this.userGender == null) {
            this.userGender = Gender.MALE; // 기본값 설정을 통해 무결성 보장
        }
        if (this.userStatus == null) {
            this.userStatus = Status.ACTIVE;
        }
        if (this.userRole == null) {
            this.userRole = Role.ROLE_USER;
        }
        this.userCreatedAt = LocalDateTime.now();
    }

}

package com.practice.community.user.repository;

import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// User 엔티티에 대한 CRUD 메소드를 자동으로 상속받음 (User : DB에서 관리되는 엔티티, Long : 해당 엔티티의 기본 키 타입)
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 ID와 상태값을 기준으로 조회 (Optional : 단일 객체로 반환하며 객체가 없을 경우 Optional로 감싸서 반환)
    Optional<User> findByUserIdAndUserStatus(Long userId, Status userStatus);
    // 사용자 상태값을 기준으로 조회 (List : 여러 객체를 반환하며 객체가 없을 경우 빈 리스트로 반환)
    List<User> findByUserStatus(Status userStatus);
}

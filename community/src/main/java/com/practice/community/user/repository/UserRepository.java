package com.practice.community.user.repository;

import com.practice.community.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// User 엔티티에 대한 CRUD 메소드를 자동으로 상속받음 (User : DB에서 관리되는 엔티티, Long : 해당 엔티티의 기본 키 타입)
public interface UserRepository extends JpaRepository<User, Long> {

}

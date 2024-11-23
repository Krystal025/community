package com.practice.community.user.service;

import com.practice.community.exception.ErrorCode;
import com.practice.community.user.dto.CustomUserDetails;
import com.practice.community.user.entity.User;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 사용자 정보를 조회하는 서비스
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 로그인 시도시 DB로부터 사용자 이름(이메일)을 통해 사용자 정보를 불러옴
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        System.out.println("Requested Email: " + userEmail);
        User user = userRepository.findByUserEmail(userEmail);
        // 사용자가 존재하지 않으면 예외를 던짐
        if (user == null) {
            throw new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.toString());
        }
        // DB에서 불러온 User 엔티티를 바탕으로 UserDetails 객체를 생성하여 반환
        return CustomUserDetails.fromUser(user);
    }
}

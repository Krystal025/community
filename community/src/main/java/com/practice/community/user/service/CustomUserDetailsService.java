package com.practice.community.user.service;

import com.practice.community.user.dto.CustomUserDetails;
import com.practice.community.user.entity.User;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail);
        // 유저가 존재하지 않으면 예외를 던짐
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }
        return new CustomUserDetails(user);
    }
}

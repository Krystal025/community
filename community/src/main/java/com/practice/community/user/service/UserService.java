package com.practice.community.user.service;

import com.practice.community.user.dto.UserDto;
import com.practice.community.user.entity.User;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 사용자 등록
    public void saveUser(UserDto userDto){
        User.Gender userGender;
        if(userDto.getUserGender() == null){
            userGender = User.Gender.MALE;
        }else{
            userGender = userDto.getUserGender();
        }
        User user = new User(
                userDto.getUserName(),
                userDto.getUserEmail(),
                userDto.getUserPwd(),
                userDto.getUserNickname(),
                userGender,
                userDto.getUserBirthday()
        );
        userRepository.save(user);
    }

    // 사용자 목록 조회
    public List<UserDto> getList() {
        return userRepository.findAll() // DB에서 모든 User 엔티티를 가져와 List<T>로 반환
                .stream() // List를 stream으로 변환
                .filter(user -> user.getUserStatus() == User.Status.ACTIVE) // 상태가 ACTIVE인 사용자만 필터링
                .map(UserDto::new) // stream의 각 요소를 변환 (User 객체를 매개변수로 받아 UserDto로 변환)
                .collect(Collectors.toList()); // stream의 요소를 수집하여 특정 List 컬렉션으로 변환
    }

    // 사용자 정보 수정
    @Transactional // 트랜잭션이 성공적으로 완료되면 변경사항이 자동으로 커밋되어 DB에 반영됨
    public void updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId) // 영속성 컨텍스트에 사용자가 존재하는지 확인
                .orElseThrow(() -> new RuntimeException("User Not Found")); // 사용자 Id가 없으면 에러 처리
        user.updateUser(userDto.getUserEmail(), // 영속성 컨텍스트를 통해
                userDto.getUserPwd(),
                userDto.getUserNickname());
    }

    // 사용자 비활성화(탈퇴)
    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        user.deactivateUser();
    }

}

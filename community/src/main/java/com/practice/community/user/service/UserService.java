package com.practice.community.user.service;

import com.practice.community.exception.custom.EmailAlreadyExistsException;
import com.practice.community.exception.ErrorCode;
import com.practice.community.exception.custom.NicknameAlreadyExistsException;
import com.practice.community.exception.custom.UserNotFoundException;
import com.practice.community.user.dto.UserRequestDto;
import com.practice.community.user.dto.UserResponseDto;
import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Status;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 사용자 등록
    public void saveUser(UserRequestDto userRequestDto){
        try{ // DTO를 엔티티로 변환하여 DB에 저장함
            User user = User.builder()
                    .userName(userRequestDto.getUserName())
                    .userEmail(userRequestDto.getUserEmail())
                    .userPwd(userRequestDto.getUserPwd())
                    .userNickname(userRequestDto.getUserNickname())
                    .userGender(userRequestDto.getUserGender())
                    .userBirthday(userRequestDto.getUserBirthday())
                    .build();
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            String errorMessage = e.getCause().getMessage();
            if(errorMessage.contains("user.user_email")){
                throw new EmailAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
            if(errorMessage.contains("user.user_nickname")){
                throw new NicknameAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
            }
            throw new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    // 사용자 목록 조회
    public List<UserResponseDto> getList() {
        return userRepository.findAll() // DB에서 모든 엔티티(User)를 가져와 List<User>로 반환
                .stream() // List<User>를 스트림으로 변환
                .filter(user -> user.getUserStatus() == Status.ACTIVE) // 상태가 ACTIVE인 사용자만 필터링
                .map(UserResponseDto::new) // 스트림의 각 요소를 변환 (User 객체를 매개변수로 받아 UserDto로 변환)
                .toList(); // 스트림의 요소를 수집하여 List 컬렉션 타입으로 변환
    }

    // 사용자 정보 조회
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(user);
    }

    // 사용자 정보 수정
    @Transactional // 트랜잭션이 성공적으로 완료되면 변경사항이 자동으로 커밋되어 DB에 반영됨
    public void updateUser(Long userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId) // 영속성 컨텍스트에 사용자가 존재하는지 확인
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)); // 사용자 Id가 없으면 에러 처리
        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(userRequestDto.getUserEmail()) &&
                !userRequestDto.getUserEmail().equals(user.getUserEmail())) {
            throw new EmailAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        // 닉네임 중복 체크
        if (userRepository.existsByUserNickname(userRequestDto.getUserNickname()) &&
                !userRequestDto.getUserNickname().equals(user.getUserNickname())) {
            throw new NicknameAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
        // 기존 객체의 상태를 유지하면서 일부 필드만 수정
        User updatedUser = user.toBuilder()  // 기존 객체를 기반으로 빌더 사용 (일부 필드 수정 가능)
                .userEmail(userRequestDto.getUserEmail())
                .userPwd(userRequestDto.getUserPwd())
                .userNickname(userRequestDto.getUserNickname())
                .build();
        userRepository.save(updatedUser);
    }

    // 사용자 비활성화(탈퇴)
    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        User deactivateUser = user.toBuilder()
                .userStatus(Status.INACTIVE) // 상태 변경
                .build();
        userRepository.save(deactivateUser);
    }

}

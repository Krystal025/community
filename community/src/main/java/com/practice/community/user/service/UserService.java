package com.practice.community.user.service;

import com.practice.community.exception.custom.EmailAlreadyExistsException;
import com.practice.community.exception.ErrorCode;
import com.practice.community.exception.custom.NicknameAlreadyExistsException;
import com.practice.community.exception.custom.UnauthorizedAccessException;
import com.practice.community.exception.custom.UserNotFoundException;
import com.practice.community.user.dto.UserRequestDto;
import com.practice.community.user.dto.UserResponseDto;
import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Status;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 사용자 등록
    public void saveUser(UserRequestDto userRequestDto){
        // 이메일 중복 체크
        if(userRepository.existsByUserEmail(userRequestDto.getUserEmail())){
            throw new EmailAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        // 닉네임 중복 체크
        if(userRepository.existsByUserNickname(userRequestDto.getUserNickname())){
            throw new NicknameAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
        try{ // DTO를 엔티티로 변환하여 DB에 저장
            User user = User.builder()
                    .userName(userRequestDto.getUserName())
                    .userEmail(userRequestDto.getUserEmail())
                    .userPwd(passwordEncoder.encode(userRequestDto.getUserPwd()))
                    .userNickname(userRequestDto.getUserNickname())
                    .userGender(userRequestDto.getUserGender())
                    .userBirthday(userRequestDto.getUserBirthday())
                    .build();
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    // 사용자 목록 조회
    public List<UserResponseDto> getList() {
        return userRepository.findAll() // DB에서 모든 엔티티(User)를 가져와 List<User>로 반환
                .stream() // List<User>를 스트림으로 변환
                .filter(user -> user.getUserStatus() == Status.ACTIVE) // 상태가 ACTIVE인 사용자만 필터링
                .map(user -> UserResponseDto.builder() // 스트림의 각 요소를 변환 (User 객체를 매개변수로 받아 UserDto로 변환)
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .userEmail(user.getUserEmail())
                        .userNickname(user.getUserNickname())
                        .userGender(user.getUserGender())
                        .userBirthday(user.getUserBirthday())
                        .userStatus(user.getUserStatus())
                        .userCreatedAt(user.getUserCreatedAt())
                        .build())
                .toList(); // 스트림의 요소를 수집하여 List 컬렉션 타입으로 변환
    }

    // 사용자 정보 조회
    public UserResponseDto getUser(Long userId) {
        // 현재 인증된 사용자 이메일
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        // 요청한 사용자 이메일
        String userEmail = user.getUserEmail().replaceAll("\\s+", "").trim();
        // JWT에서 추출한 이메일과 요청 이메일 비교
        validateEmail(loggedInEmail, userEmail);
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userNickname(user.getUserNickname())
                .userGender(user.getUserGender())
                .userBirthday(user.getUserBirthday())
                .userCreatedAt(user.getUserCreatedAt())
                .build();
    }

    // 사용자 정보 수정
    @Transactional // 트랜잭션이 성공적으로 완료되면 변경사항이 자동으로 커밋되어 DB에 반영됨
    public void updateUser(Long userId, UserRequestDto userRequestDto) {
        // 현재 인증된 사용자 이메일
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // 영속성 컨텍스트에 사용자가 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        // 요청한 사용자 이메일
        String userEmail = user.getUserEmail().replaceAll("\\s+", "").trim();
        // JWT에서 추출한 이메일과 요청 이메일 비교
        validateEmail(loggedInEmail, userEmail);
        // 닉네임 중복 체크
        if (userRequestDto.getUserNickname() != null &&
                userRepository.existsByUserNickname(userRequestDto.getUserNickname()) &&
                !userRequestDto.getUserNickname().equals(user.getUserNickname())) {
            throw new NicknameAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
        // 기존 객체의 상태를 유지하면서 일부 필드만 수정
        User updatedUser = user.toBuilder()  // 기존 객체를 기반으로 빌더 사용 (일부 필드 수정 가능)
                .userPwd(userRequestDto.getUserPwd() != null ? passwordEncoder.encode(userRequestDto.getUserPwd()) : user.getUserPwd())
                .userNickname(userRequestDto.getUserNickname() != null ? userRequestDto.getUserNickname() : user.getUserNickname())
                .build();
        userRepository.save(updatedUser);
    }

    // 사용자 비활성화(탈퇴)
    @Transactional
    public void deactivateUser(Long userId) {
        // 인증된 사용자의 권한
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        // 현재 인증된 사용자 이메일
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // 영속성 컨텍스트에 사용자가 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        // 요청한 사용자 이메일
        String userEmail = user.getUserEmail().replaceAll("\\s+", "").trim();
        // JWT에서 추출한 이메일과 요청 이메일이 같거나, 관리자인 경우에만 비활성화 가능
        if (!userEmail.equals(loggedInEmail) && !isAdmin) {
            throw new UnauthorizedAccessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        User deactivateUser = user.toBuilder()
                .userStatus(Status.INACTIVE) // 상태 변경
                .build();
        userRepository.save(deactivateUser);
    }

    // 이메일 비교 메소드 (추후 AOP로 분리)
    private void validateEmail(String loggedInEmail, String requestedEmail) {
        if (!loggedInEmail.equals(requestedEmail)) {
            throw new UnauthorizedAccessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}

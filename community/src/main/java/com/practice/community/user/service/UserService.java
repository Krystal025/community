package com.practice.community.user.service;

import com.practice.community.user.dto.UserDto;
import com.practice.community.user.entity.User;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserDto userDto){

        User.Gender gender;
        if(userDto.getUserGender() == null || userDto.getUserGender().isEmpty()){
            gender = User.Gender.MALE;
        }else{
            gender = User.Gender.valueOf(userDto.getUserGender().toUpperCase());
        }
        User user = new User(
                userDto.getUserName(),
                userDto.getUserEmail(),
                userDto.getUserPwd(),
                userDto.getUserNickname(),
                gender,
                userDto.getUserBirthday()
        );
        userRepository.save(user);
    }
}

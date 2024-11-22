package com.practice.community.user.service;

import com.practice.community.user.dto.*;
import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Role;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // OAuth2 인증과정에서 사용자 정보를 가져옴
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 사용자 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);
        // 어떤 소셜 로그인 제공자에서 요청이 왔는지 확인
        OAuth2Response oAuth2Response = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 제공자가 Google인 경우 사용자 데이터를 GoogleResponseDto로 변환
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponseDto(oAuth2User.getAttributes());
        } else {
            return null;
        }
        // 소셜 제공자 이름과 사용자 식별 ID를 조합하여 고유 ID 생성
        String socialUserId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        // OAuth2 로그인을 통해 사용자 정보를 받아 해당 사용자가 존재하는지 확인
        User existData = userRepository.findBySocialUserId(socialUserId);
        // 이미 존재하는 사용자인지 확인
        if(existData == null){
            User user = User.builder() // 새로운 사용자 객체 생성
                    .socialUserId(socialUserId)
                    .userName(oAuth2Response.getName()) // OAuth에서 가져온 사용자 이름
                    .userEmail(oAuth2Response.getEmail()) // OAuth에서 가져온 이메일
                    .userNickname(oAuth2Response.getName()) // 기본 닉네임으로 사용자 이름 사용
                    .build();
            userRepository.save(user); // 사용자 정보 저장
            OAuth2InfoDto oAuth2InfoDto = OAuth2InfoDto.builder()
                    .socialUserId(socialUserId)
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_USER.toString()) // 클라이언트에게 전달한 사용자 Role
                    .build();
            return new CustomOAuth2User(oAuth2InfoDto); // 클라이언트에게 전달되는 정보
        }else{
            boolean isNameUpdated = !existData.getUserName().equals(oAuth2Response.getName());
            boolean isEmailUpdated = !existData.getUserEmail().equals(oAuth2Response.getEmail());
            if (isNameUpdated || isEmailUpdated) {
                existData.setUserName(oAuth2Response.getName()); // 이름 갱신
                existData.setUserEmail(oAuth2Response.getEmail()); // 이메일 갱신
                userRepository.save(existData); // 수정된 사용자 정보 저장
            }
            OAuth2InfoDto oAuth2InfoDto = OAuth2InfoDto.builder()
                    .socialUserId(existData.getSocialUserId())
                    .name(oAuth2Response.getName())
                    .role(existData.getUserRole().toString())
                    .build();
            return new CustomOAuth2User(oAuth2InfoDto); // 클라이언트에게 전달되는 정보
        }
    }
}

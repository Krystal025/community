package com.practice.community.user.service;

import com.practice.community.exception.ErrorCode;
import com.practice.community.exception.custom.UnsupportedOAuth2Provider;
import com.practice.community.user.dto.*;
import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Role;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 소셜 로그인 성공시 호출되는 메소드 (사용자 등록/업데이트 및 인증/인가에 사용될 사용자 정보 객체 반환)
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2에서 사용자 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);
        // 어떤 소셜 로그인 제공자에서 요청이 왔는지 확인
        OAuth2Response oAuth2Response = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 제공자가 Google인 경우 사용자 데이터를 GoogleResponse 객체로 변환
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            throw new UnsupportedOAuth2Provider(ErrorCode.UNSUPPORTED_OAUTH2_PROVIDER);
        }
        // 소셜 제공자 이름과 사용자 식별 ID를 조합하여 고유 ID 생성
        String socialId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        // OAuth2RequestDto로 변환 (DTO에서 검증할 수 있도록 함)
        OAuth2RequestDto oAuth2RequestDto = OAuth2RequestDto.builder()
                .userName(oAuth2Response.getUserName())
                .userEmail(oAuth2Response.getUserEmail())
                .userNickname(oAuth2Response.getUserName())
                .build();
        // OAuth2 로그인을 통해 사용자 정보를 받아 해당 사용자가 존재하는지 확인
        User existData = userRepository.findBySocialId(socialId);
        // 새로운 사용자일 경우
        if(existData == null){
            User user = User.builder()
                    .userName(oAuth2RequestDto.getUserName())
                    .userEmail(oAuth2RequestDto.getUserEmail())
                    .userNickname(oAuth2RequestDto.getUserNickname()) // 기본 닉네임으로 사용자 이름 사용
                    .socialId(socialId)
                    .provider(oAuth2Response.getProvider())
                    .build();
            userRepository.save(user); // 새로운 사용자 DB에 저장
            OAuth2Info oAuth2Info = OAuth2Info.builder()
                    .userId(user.getUserId())
                    .userName(oAuth2RequestDto.getUserName())
                    .userEmail(oAuth2RequestDto.getUserEmail())
                    .userRole(Role.ROLE_USER) // 클라이언트에게 전달한 사용자 Role
                    .socialId(socialId)
                    .provider(oAuth2Response.getProvider())
                    .build();
            return new CustomOAuth2User(oAuth2Info); // 인증/인가 작업에 사용될 사용자 정보 객체 반환
        }
        // 기존 사용자일 경우
        else{
            // 변경된 정보가 있을 경우 업데이트
            boolean isUpdated = false;
            if (!existData.getUserName().equals(oAuth2RequestDto.getUserName())) {
                existData.setUserName(oAuth2RequestDto.getUserName());
                isUpdated = true;
            }
            if (!existData.getUserEmail().equals(oAuth2RequestDto.getUserEmail())) {
                existData.setUserEmail(oAuth2RequestDto.getUserEmail());
                isUpdated = true;
            }
            if (isUpdated) {
                userRepository.save(existData); // 수정된 사용자 정보 저장
            }
            OAuth2Info oAuth2Info = OAuth2Info.builder()
                    .userId(existData.getUserId())
                    .userName(oAuth2RequestDto.getUserName())
                    .userEmail(oAuth2RequestDto.getUserEmail())
                    .userRole(existData.getUserRole())
                    .socialId(existData.getSocialId())
                    .provider(existData.getProvider())
                    .build();
            return new CustomOAuth2User(oAuth2Info); // 인증/인가 작업에 사용될 사용자 정보 객체 반환
        }
    }
}

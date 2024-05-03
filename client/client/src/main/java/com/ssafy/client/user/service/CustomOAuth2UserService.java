package com.ssafy.client.user.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ssafy.client.user.OAuth2Response.GoogleResponse;
import com.ssafy.client.user.OAuth2Response.KakaoResponse;
import com.ssafy.client.user.OAuth2Response.NaverResponse;
import com.ssafy.client.user.OAuth2Response.OAuth2Response;
import com.ssafy.client.user.OAuth2Response.SsafyResponse;
import com.ssafy.client.user.domain.CustomOAuth2User;
import com.ssafy.client.user.domain.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("{} " , userRequest.getAdditionalParameters());
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 세션에 임시로 저장하자
        HttpServletRequest curRequest =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = curRequest.getSession();
        session.setAttribute("access_token", userRequest.getAccessToken().getTokenValue());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId = " + registrationId);

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")){

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("client")) {
            oAuth2Response = new SsafyResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername((String) oAuth2User.getAttributes().get("email"));
        log.info("ttt {}", oAuth2User.getAttributes().get("email"));
        userDTO.setName(oAuth2User.getName());
        log.info("ttt {} , ", oAuth2User.getName());
        userDTO.setRole("USER_ROLE");
        return new CustomOAuth2User(userDTO);
        // UserEntity existData = userRepository.findByUsername(username);
        //
        // if (existData == null) {
        //
        //     UserEntity userEntity = new UserEntity();
        //     userEntity.setUsername(username);
        //     userEntity.setEmail(oAuth2Response.getEmail());
        //     userEntity.setName(oAuth2Response.getName());
        //     userEntity.setRole("ROLE_USER");
        //
        //     userRepository.save(userEntity);
        //
        //     userEntity = userRepository.findByUsername(username);
        //     // 오늘 출석하지 않았으면 골드 지급
        //     if (userService.checkAttendance(userEntity.getId())) {
        //         userRepository.addGoldById(userEntity.getId());
        //     }
        //
        //     UserDTO userDTO = new UserDTO();
        //     userDTO.setUsername(username);
        //     userDTO.setName(oAuth2Response.getName());
        //     userDTO.setRole("ROLE_USER");
        //
        //     return new CustomOAuth2User(userDTO);
        // }
        // else {
        //
        //     existData.setEmail(oAuth2Response.getEmail());
        //     existData.setName(oAuth2Response.getName());
        //
        //     userRepository.save(existData);
        //
        //     if (userService.checkAttendance(existData.getId())) {
        //         userRepository.addGoldById(existData.getId());
        //     }
        //
        //     UserDTO userDTO = new UserDTO();
        //     userDTO.setUsername(existData.getUsername());
        //     userDTO.setName(oAuth2Response.getName());
        //     userDTO.setRole(existData.getRole());
        //
        //     return new CustomOAuth2User(userDTO);
        // }
    }
}

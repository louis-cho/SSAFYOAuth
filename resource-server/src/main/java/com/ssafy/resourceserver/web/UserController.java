package com.ssafy.resourceserver.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public UserInfo getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        // Jwt에서 사용자 정보 추출
        String username = jwt.getClaimAsString("sub");
        String scope = jwt.getClaimAsString("scope");
        // 필요한 사용자 정보를 UserInfo 객체에 설정
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setSub(username);


        userInfo.setScope(scope);
        // 다른 사용자 정보들도 설정 가능

        return userInfo;
    }

    @Getter
    @Setter
    public static class UserInfo {
        private String username;
        private String sub;
        private String scope;
        // getter and setter
    }
}
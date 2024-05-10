package com.ssafy.client.user.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ssafy.client.user.service.ApiService;
import com.ssafy.client.user.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class TestController {
    private final TokenService tokenService;
    private final ApiService apiService;

    @GetMapping("/test")
    public String index(OAuth2AuthenticationToken authentication) {
        try {
            String accessToken = tokenService.getAccessToken(authentication);

            String refreshToken = tokenService.getRefreshToken(authentication);
            System.out.println(accessToken + " tttt");
            System.out.println(refreshToken + "tttttttt");
            String s = apiService.callProtectedApi("https://ssafyauth-resource.duckdns.org/api/team");
            log.info("{} ttt",s);
        } catch (Exception e) {
            log.error("Error while retrieving access token: ", e);
        }
        return "index";
    }

}

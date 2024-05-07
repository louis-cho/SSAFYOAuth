package com.ssafy.client.user.controller;

import com.ssafy.client.user.service.ApiService;
import com.ssafy.client.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
            System.out.println(accessToken + " tttt");
            String s = apiService.callProtectedApi("http://127.0.0.1:8090/api/team");
            log.info("{} ttt",s);
        } catch (Exception e) {
            log.error("Error while retrieving access token: ", e);
        }
        return "index";
    }

}

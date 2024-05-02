package com.ssafy.resourceserver.member.controller;

import com.ssafy.resourceserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final MemberService memberService;

    @GetMapping("/info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        // Jwt에서 사용자 정보 추출
        log.info("test : {} ",jwt.getTokenValue());
        log.info("{} JWT TTT", jwt);
        String email = jwt.getClaimAsString("sub");
        List<String> scopes = jwt.getClaimAsStringList("scope");

        Map<String, Object> userProfile = memberService.getUserProfile(email, scopes);
        log.info("유저정보 스코프별: {} ",userProfile);
        return userProfile;
    }
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Jwt jwt) {
        // Jwt에서 사용자 정보 추출
        log.info("test : {} ",jwt.getTokenValue());
        String username = jwt.getClaimAsString("sub");
        String scope = jwt.getClaimAsString("scope");
        log.info("test {} {}" , username,scope);

        return "잘되네";
    }

}
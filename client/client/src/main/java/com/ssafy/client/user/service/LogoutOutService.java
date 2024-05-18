package com.ssafy.client.user.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Component
public class LogoutOutService {
    private final RestTemplate restTemplate;


    public LogoutOutService(@Qualifier("logoutRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Value("${spring.security.oauth2.client.registration.ssafyOAuth.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.ssafyOAuth.client-secret}")
    private String clientSecret;

    public void revokeToken(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        // 클라이언트 인증을 위한 기본 인증 헤더 설정
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;
        headers.set("Authorization", authHeader);

        // 요청 바디에 토큰 포함
        String requestBody = String.format("token=%s&token_type_hint=refresh_token", token);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Token revoked successfully.");
            System.out.println(response.getBody());

        } else {
            System.out.println("Failed to revoke token. Response: " + response.getStatusCode());
        }
    }

    public OAuth2AccessToken refreshAccessToken(String tokenUri, OAuth2RefreshToken refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        String body = String.format("grant_type=refresh_token&refresh_token=%s", refreshToken.getTokenValue());
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);
        System.out.println(response);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            String accessToken = (String) responseBody.get("access_token");
            String tokenType = (String) responseBody.get("token_type");
            Instant expiresAt = Instant.now().plusSeconds(((Number) responseBody.get("expires_in")).longValue());

            return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(), expiresAt);
        } else {
            throw new RuntimeException("Failed to refresh access token");
        }
    }

    public void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        // Remove cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        response.sendRedirect("/login");
    }
}

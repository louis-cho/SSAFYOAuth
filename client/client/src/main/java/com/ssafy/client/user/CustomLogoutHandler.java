package com.ssafy.client.user;

import com.ssafy.client.user.service.LogoutOutService;
import com.ssafy.client.user.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final LogoutOutService logoutOutService;
    private static final String RESOURCE_SERVER_URL = "http://127.0.0.1:9000/oauth2/revoke";
    private final TokenService tokenService;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = null;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            refreshToken = tokenService.getRefreshToken(oauthToken);
        }
        if (refreshToken != null) {
            logoutOutService.revokeToken(RESOURCE_SERVER_URL, refreshToken);
            try {
                logoutOutService.handleLogout(request, response);
            } catch (IOException e) {
                throw new RuntimeException("로그아웃 중 에러발생");
            }
        }
    }

}

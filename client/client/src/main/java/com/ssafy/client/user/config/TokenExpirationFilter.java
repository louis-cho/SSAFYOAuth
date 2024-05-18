package com.ssafy.client.user.config;

import com.ssafy.client.user.service.LogoutOutService;
import com.ssafy.client.user.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenExpirationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final LogoutOutService logoutOutService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final String TOKEN_URL = "http://localhost:9000/oauth2/token";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            OAuth2AccessToken accessToken = tokenService.getAccessTokenObject(authentication);
            if (accessToken.getExpiresAt().isBefore(Instant.now())) {
                OAuth2User user = authentication.getPrincipal();
                OAuth2RefreshToken refreshToken = tokenService.getRefreshTokenObject(authentication);
                if (refreshToken != null) {
                    try {
                        OAuth2AccessToken newAccessToken = logoutOutService.refreshAccessToken(TOKEN_URL,refreshToken);
                        String registrationId = authentication.getAuthorizedClientRegistrationId();
                        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

                        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                                clientRegistration,
                                authentication.getName(),
                                newAccessToken,
                                refreshToken);

                        tokenService.saveTokenData(authorizedClient, authentication);

                        // Update the authentication with the new access token
                        OAuth2AuthenticationToken newAuthentication = new OAuth2AuthenticationToken(
                                user, authentication.getAuthorities(), registrationId);
                        newAuthentication.setDetails(newAccessToken);
                        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
//                        Cookie accessCookie = createCookie("access_token", newAccessToken.getTokenValue());
//                        response.addCookie(accessCookie);

                    } catch (Exception e) {
                        logoutOutService.handleLogout(request, response);
                        return;
                    }
                } else {
                    logoutOutService.handleLogout(request, response);
                    return;
                }
            }
        }
//        if (authentication != null && authentication.getCredentials() instanceof OAuth2AccessToken) {
//            OAuth2AccessToken accessToken = (OAuth2AccessToken) authentication.getCredentials();
//            if (accessToken.getExpiresAt().isBefore(Instant.now())) {
//                SecurityContextHolder.clearContext();
//                request.getSession().invalidate();
//                response.sendRedirect("/login"); // 로그인 페이지로 리디렉트
//                return;
//            }
//        }
        filterChain.doFilter(request, response);
    }
    public static Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");

        return cookie;
    }

}

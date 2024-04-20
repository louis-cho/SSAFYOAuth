package com.ssafy.client.user;

import com.ssafy.client.user.jwt.JWTUtil;
import com.ssafy.client.user.service.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final JWTService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info(authentication.getPrincipal().toString());

        if (authentication.getPrincipal() == null) {
            log.info("null 입니다.");
            return;
        }

        String username;
        String role;
        if (authentication.getPrincipal() instanceof OidcUser) {
            // OIDC 로그인 처리
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            username = oidcUser.getName(); // OIDC 기반으로 사용자명 얻기
            role = oidcUser.getAuthorities().iterator().next().getAuthority(); // 권한 정보
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            // OAuth2 로그인 처리
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            username = oAuth2User.getName(); // OAuth2 기반으로 사용자명 얻기
            role = oAuth2User.getAuthorities().iterator().next().getAuthority(); // 권한 정보
        } else {
            log.error("지원하지 않는 인증 타입입니다.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        log.info("tetetete + {} {} ", username,role);
        // 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        log.info("access = " + access);
        log.info("refresh = " + refresh);

        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("https://j10a710.p.ssafy.io");
    }

    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}

package com.ssafy.client.user;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ssafy.client.user.domain.CustomOAuth2User;
// import com.ssafy.client.client.user.jwt.JWTUtil;
import com.ssafy.client.user.jwt.JWTUtil;
import com.ssafy.client.user.service.JWTService;
import com.ssafy.client.user.service.TokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
        log.info("{} what is this", authentication.getPrincipal());
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 세션 -> 쿠키에다가 이사하기
        HttpSession session = request.getSession(false);
        String accessToken = (session != null) ? (String) session.getAttribute("access_token") : null;

        if (accessToken != null) {
            // 쿠키 생성하고 넣기
            Cookie accessCookie = createCookie("access_token", accessToken);
            response.addCookie(accessCookie);
        } else {
            log.warn("Access token not found in session");
        }


//        response.setHeader("access", access);
        log.info("{} what is this", authentication.getPrincipal());
        // response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("https://k10a306.p.ssafy.io");
    }

    public static Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");

        return cookie;
    }
}

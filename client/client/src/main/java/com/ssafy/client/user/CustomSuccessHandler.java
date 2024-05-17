package com.ssafy.client.user;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.sql.rowset.spi.SyncResolver;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.client.user.domain.CustomOAuth2User;
// import com.ssafy.client.client.user.jwt.JWTUtil;
import com.ssafy.client.user.dto.TempDto;
import com.ssafy.client.user.jwt.JWTUtil;
import com.ssafy.client.user.service.ApiService;
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
    public final ApiService apiService;
    private final ObjectMapper objectMapper;
    private static final String RESOURCE_SERVER_URL = "http://localhost:8090/";
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
        log.info("통과 ");
        // API 호출을 통해 사용자 존재 유무를 체크
        String result = apiService.callProtectedApi(RESOURCE_SERVER_URL + "user/check");
        TempDto tempDto = objectMapper.readValue(result, TempDto.class);
        if (tempDto.isRes()) {
            // 사용자가 존재하는 경우
            HttpSession session = request.getSession(false);
            String accessToken = (session != null) ? (String) session.getAttribute("access_token") : null;

            if (accessToken != null) {
                // 액세스 토큰이 세션에 존재하면 쿠키를 생성하여 응답에 추가
                Cookie accessCookie = createCookie("access_token", accessToken);
                response.addCookie(accessCookie);
            } else {
                // 액세스 토큰이 세션에 존재하지 않으면 경고 로그
                log.warn("Access token not found in session");
            }

            // 인증된 사용자의 정보 로깅
            log.info("{} what is this", authentication.getPrincipal());

            // 성공 응답 설정 후 리다이렉트
            response.setStatus(HttpStatus.OK.value());
            response.sendRedirect("https://k10a306.p.ssafy.io");
        } else {
            // 사용자가 존재하지 않는 경우, 회원가입 페이지로 리다이렉트
            response.setStatus(HttpStatus.OK.value());
            response.sendRedirect("http://localhost:8080/user/sign-up/" + tempDto.getSeq());
        }


    }

    public static Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");

        return cookie;
    }
}

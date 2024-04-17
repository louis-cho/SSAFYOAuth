package com.ssafy.client.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2FailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // 요청 정보와 예외 메시지를 로깅
        log.error("인증 실패: {}, 요청 정보: {}, 응답 정보: {}", exception.getMessage(), request, response);

        // 스택 트레이스를 로깅
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();
        log.error("스택 트레이스: {}", stackTrace);

        // 클라이언트에게 에러 메시지 전송
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{\"error\": \"인증 실패\", \"message\": \"" + exception.getMessage() + "\"}");
    }
}

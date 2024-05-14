package com.ssafy.authorization.filter;

import jakarta.servlet.RequestDispatcher;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private String redirectUrl;

    public CustomAuthenticationSuccessHandler() {
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        response.sendRedirect("https://localhost:8080/asdf");
        // 리다이렉트할 URL 설정
//        if (redirectUrl != null && !redirectUrl.isEmpty()) {
//            response.sendRedirect("www.naver.com");
//        } else {
//            super.onAuthenticationSuccess(request, response, authentication);
//        }
    }
}

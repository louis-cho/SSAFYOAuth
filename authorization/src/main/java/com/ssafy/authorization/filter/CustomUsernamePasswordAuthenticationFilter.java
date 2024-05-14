package com.ssafy.authorization.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    @Autowired
    public CustomUsernamePasswordAuthenticationFilter(@Lazy AuthenticationManager authenticationManager,  CustomAuthenticationSuccessHandler successHandler,
                                                      CustomAuthenticationFailureHandler failureHandler) {
        setAuthenticationManager(authenticationManager);
        this.successHandler = successHandler;
        this.successHandler.setRedirectUrl("/lllll");
        this.failureHandler = failureHandler;
        this.failureHandler.setRedirectUrl("/ffffff");
    }

    public void setCustomAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Custom authentication logic here, if needed
        // Example: Validate request parameters, check in Redis, etc.

        // Call the super method to handle default authentication
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        // Custom logic after successful authentication
        try {
            successHandler.onAuthenticationSuccess(request, response, authResult);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // Custom logic after unsuccessful authentication
        try {
            failureHandler.onAuthenticationFailure(request, response, failed);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}

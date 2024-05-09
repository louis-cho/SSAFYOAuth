package com.ssafy.client.user.config;

import com.ssafy.client.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2Interceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        OAuth2AuthenticationToken authenticationToken = getAuthenticationToken();

        if (authenticationToken != null) {
            String accessToken = tokenService.getAccessToken(authenticationToken);
            request.getHeaders().setBearerAuth(accessToken);
            log.info("API 쏘기전 token {} ",accessToken);

        }
        log.info("API 쏘기전 request {} ",request.getURI());

        return execution.execute(request, body);

    }

    private OAuth2AuthenticationToken getAuthenticationToken() {
        // Retrieves the current authentication from the security context
        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken) {
            return (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        }
        return null;
    }
}

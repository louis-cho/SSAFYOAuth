package com.ssafy.authorization.filter;

import com.ssafy.authorization.stats.login.model.LoginStats;
import com.ssafy.authorization.stats.login.service.LoginStatsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Enumeration;
import java.util.Map;

@Component
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final LoginStatsService loginStatsService;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RegisteredClientRepository registeredClientRepository;

    @Autowired
    public CustomUsernamePasswordAuthenticationFilter(@Lazy AuthenticationManager authenticationManager, CustomAuthenticationSuccessHandler successHandler,
                                                      CustomAuthenticationFailureHandler failureHandler, LoginStatsService loginStatsService
            , @Lazy RegisteredClientRepository registeredClientRepository) {

        setAuthenticationManager(authenticationManager);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.loginStatsService = loginStatsService;
        this.registeredClientRepository = registeredClientRepository;
    }

    public void setCustomAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Custom logic after successful authentication
        SavedRequest savedRequest = requestCache.getRequest(request, null);

        // SavedRequest 객체가 존재하면 파라미터 맵에서 client_id 추출
        if (savedRequest != null) {
            Map<String, String[]> parameterMap = savedRequest.getParameterMap();
            String[] clientIds = parameterMap.get("client_id");
            if (clientIds != null && clientIds.length > 0) {
                String clientId = clientIds[0];
                String email = authResult.getName();
                RegisteredClient client = registeredClientRepository.findByClientId(clientId);
                LoginStats loginStats = new LoginStats(email, client.getId(), Instant.now());
                loginStats.setSuccess(true);
                loginStatsService.save(loginStats);
            }
        }
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // Custom logic after unsuccessful authentication
        try {
            SavedRequest savedRequest = requestCache.getRequest(request, null);

            // SavedRequest 객체가 존재하면 파라미터 맵에서 client_id 추출
            if (savedRequest != null) {
                Map<String, String[]> parameterMap = savedRequest.getParameterMap();
                String[] clientIds = parameterMap.get("client_id");
                if (clientIds != null && clientIds.length > 0) {
                    String clientId = clientIds[0];
                    String email = request.getParameter("username");
                    RegisteredClient client = registeredClientRepository.findByClientId(clientId);
                    LoginStats loginStats = new LoginStats(email, client.getId(), Instant.now());
                    loginStats.setSuccess(false);
                    loginStatsService.save(loginStats);
                }
            }
            failureHandler.onAuthenticationFailure(request, response, failed);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}

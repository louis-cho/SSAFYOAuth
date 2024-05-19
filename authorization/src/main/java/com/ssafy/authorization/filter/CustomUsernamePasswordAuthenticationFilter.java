package com.ssafy.authorization.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.ssafy.authorization.stats.login.model.LoginStats;
import com.ssafy.authorization.stats.login.service.LoginStatsService;
import com.ssafy.authorization.team.entity.BlockedCountriesEntity;
import com.ssafy.authorization.team.repository.BlockedCountriesRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final LoginStatsService loginStatsService;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RegisteredClientRepository registeredClientRepository;
    private final BlockedCountriesRepository blockedCountriesRepository;

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public CustomUsernamePasswordAuthenticationFilter(@Lazy AuthenticationManager authenticationManager, CustomAuthenticationSuccessHandler successHandler,
                                                      CustomAuthenticationFailureHandler failureHandler, LoginStatsService loginStatsService
            , @Lazy RegisteredClientRepository registeredClientRepository, BlockedCountriesRepository blockedCountriesRepository
    , RedisTemplate<String, String> redisTemplate) {
		this.blockedCountriesRepository = blockedCountriesRepository;

		setAuthenticationManager(authenticationManager);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.loginStatsService = loginStatsService;
        this.registeredClientRepository = registeredClientRepository;
        this.redisTemplate =redisTemplate;
    }

    public void setCustomAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        SavedRequest savedRequest = requestCache.getRequest(request, null);
        String acceptLanguage = request.getHeader("Accept-Language");
        String languageCode;
        if (acceptLanguage != null && acceptLanguage.length() >= 2) {
            languageCode = acceptLanguage.substring(0, 2).toUpperCase();
        } else {
			languageCode = null;
		}
		if (savedRequest != null) {
            Map<String, String[]> parameterMap = savedRequest.getParameterMap();
            String[] clientIds = parameterMap.get("client_id");
            if (clientIds != null && clientIds.length > 0) {
                String clientId = clientIds[0];
                RegisteredClient client = registeredClientRepository.findByClientId(clientId);
                Optional<BlockedCountriesEntity> byTeamId = blockedCountriesRepository.findByTeamId(
                    Integer.parseInt(client.getId()));
                if (byTeamId.isPresent()) {
                    BlockedCountriesEntity block = byTeamId.get();
                    boolean flag = Arrays.stream(block.getCountryList().split(",")).anyMatch(e -> e.equals(languageCode));

                    if(flag){
                        redisTemplate.opsForValue().increment(String.valueOf(client.getId()));
                        throw new RuntimeException("차단된 국가입니다");
					}

                }

            }
        }

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
        else if(request != null){
            String clientId = request.getParameter("clientId");
            if (clientId != null && (!clientId.isEmpty() || !clientId.isBlank())) {
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

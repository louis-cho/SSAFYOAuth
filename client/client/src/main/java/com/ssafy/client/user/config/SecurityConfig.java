package com.ssafy.client.user.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ssafy.client.user.CustomOAuth2FailHandler;
import com.ssafy.client.user.CustomSuccessHandler;
// import com.ssafy.client.client.user.jwt.JWTFilter;
// import com.ssafy.client.client.user.jwt.JWTUtil;
import com.ssafy.client.user.jwt.JWTFilter;
import com.ssafy.client.user.service.CustomOAuth2UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomOAuth2FailHandler customOAuth2FailHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            //csrf disable
            http
                    .csrf((auth) -> auth.disable());

            //로그인 방식 disable
            http
                    .formLogin((auth) -> auth.disable());

            //HTTP Basic 인증 방식 disable
            http
                    .httpBasic((auth) -> auth.disable());

            http.addFilterBefore(new TokenExpirationFilter(), UsernamePasswordAuthenticationFilter.class);
            http.logout((logout) -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("https://k10a306.p.ssafy.io")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("access", "refresh")
            );


            http
                    .authorizeHttpRequests((request) -> request.requestMatchers(CorsUtils::isPreFlightRequest).permitAll());


            //oauth2
            http

                    .oauth2Login((oauth2) -> oauth2
                            .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                    .userService(customOAuth2UserService))
                            .successHandler(customSuccessHandler)
                            .failureHandler(customOAuth2FailHandler)
                    );

            //경로별 인가 작업
            http
                    .authorizeHttpRequests((auth) -> auth
                            .requestMatchers("/api/**","/css/**", "/favicon.ico", "/error", "/image/**", "/vendor/**","users/**").permitAll()
                        .requestMatchers("/**").permitAll()
                            .anyRequest().authenticated());

            //세션 설정 : STATELESS
            http
                    .sessionManagement((session) -> session
                            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

            http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        // CORS 설정 예시
                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        CorsConfiguration configuration = new CorsConfiguration();

                        // 모든 출처 허용
                        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                        // 모든 메소드 허용
                        configuration.setAllowedMethods(
                            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        // 허용할 헤더 설정
                        configuration.setAllowedHeaders(
                            Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Accept",
                                "X-Requested-With", "remember-me"));
                        // 브라우저가 응답에서 접근할 수 있는 헤더 설정
                        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                        // 자격 증명 허용 설정
                        configuration.setAllowCredentials(true);
                        // 사전 요청의 캐시 시간(초) 설정
                        configuration.setMaxAge(3600L);

                        // 모든 URL에 대해 CORS 설정 적용
                        source.registerCorsConfiguration("/**", configuration);

                        return configuration;
                    }
                }));

            return http.build();
        }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}

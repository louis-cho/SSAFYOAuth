package com.ssafy.client.user.config;

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
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

import com.ssafy.client.user.CustomOAuth2FailHandler;
import com.ssafy.client.user.CustomSuccessHandler;
// import com.ssafy.client.client.user.jwt.JWTFilter;
// import com.ssafy.client.client.user.jwt.JWTUtil;
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


        http.logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("https://localhost:8080")
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
                       // .authorizationEndpoint(e -> e.authorizationRequestRepository(oAuth2AuthorizationRequestRepository()))
                );

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        // .requestMatchers("/nft/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        // .requestMatchers("/users/name").permitAll()
                        // .requestMatchers("/users").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("/css/**", "/favicon.ico", "/error", "/image/**", "/vendor/**","users/**").permitAll()
                        .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:9000"));
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
                        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));


        return http.build();
    }
    // @Bean
    // OAuth2AuthorizedClientManager authorizedClientManager(
    //         ClientRegistrationRepository clientRegistrationRepository,
    //         OAuth2AuthorizedClientRepository authorizedClientRepository) {
    //
    //     OAuth2AuthorizedClientProvider authorizedClientProvider =
    //             OAuth2AuthorizedClientProviderBuilder.builder()
    //                     .authorizationCode()
    //                     .refreshToken()
    //                     .build();
    //     DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
    //             clientRegistrationRepository, authorizedClientRepository);
    //     authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
    //
    //     return authorizedClientManager;
    // }
    // @Bean
    // HttpCookieOAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository() {
    //     return new HttpCookieOAuth2AuthorizationRequestRepository();
    // }


}

package com.ssafy.resourceserver.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity
            .authorizeHttpRequests((request) -> request.requestMatchers(CorsUtils::isPreFlightRequest).permitAll());
        httpSecurity
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    // CORS 설정 예시
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    CorsConfiguration configuration = new CorsConfiguration();

                    // 모든 출처 허용
                    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                    // 모든 메소드 허용
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    // 허용할 헤더 설정
                    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With", "remember-me"));
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
            }))
            .authorizeHttpRequests(request -> request
                .requestMatchers("/user/**").hasAnyAuthority("SCOPE_profile", "SCOPE_email", "SCOPE_image")
                .requestMatchers("/userinfo").hasAnyAuthority("SCOPE_profile", "SCOPE_email", "SCOPE_image")
                .requestMatchers("/api/**").hasAnyAuthority("SCOPE_email")
                .requestMatchers("/signup", "/css/**", "/favicon.ico", "/image/**", "/api/**").permitAll())
            .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

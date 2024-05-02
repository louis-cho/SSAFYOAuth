package com.ssafy.resourceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.resourceserver.member.jwt.JWTFilter;
import com.ssafy.resourceserver.member.jwt.JWTUtil;
import com.ssafy.resourceserver.member.service.JWTService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTUtil jwtUtil;
    private final JWTService jwtService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        //csrf disable
        httpSecurity
            .csrf((auth) -> auth.disable());

        //로그인 방식 disable
        httpSecurity
            .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        httpSecurity
            .httpBasic((auth) -> auth.disable());

        // //JWTFilter 추가
        httpSecurity
            .addFilterBefore(new JWTFilter(jwtUtil, jwtService), UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/**").hasAnyAuthority("SCOPE_profile", "SCOPE_email", "SCOPE_image")
                        .requestMatchers("/userinfo").hasAnyAuthority("SCOPE_profile", "SCOPE_email", "SCOPE_image")
                    .requestMatchers("/signup","/css/**", "/favicon.ico","/image/**").permitAll())
                .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

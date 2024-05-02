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

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

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

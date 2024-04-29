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

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        httpSecurity
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/**").hasAuthority("SCOPE_profile")
                        .requestMatchers("/userinfo").hasAuthority("SCOPE_profile")
                    .requestMatchers("/signup","/css/**", "/favicon.ico","/image/**").permitAll())
                .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

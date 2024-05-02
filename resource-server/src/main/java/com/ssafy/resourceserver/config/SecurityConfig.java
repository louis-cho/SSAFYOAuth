package com.ssafy.resourceserver.config;

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

import jakarta.servlet.http.HttpServletRequest;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        httpSecurity
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:9000"));
                    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }
            }))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/**").hasAnyAuthority("SCOPE_profile", "SCOPE_email", "SCOPE_image")
                        .requestMatchers("/userinfo").hasAnyAuthority("SCOPE_profile", "SCOPE_email", "SCOPE_image")
                    .requestMatchers("/api/**").permitAll()
                    .requestMatchers("/signup","/css/**", "/favicon.ico","/image/**").permitAll())
                .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

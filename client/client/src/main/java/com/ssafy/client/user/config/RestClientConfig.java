package com.ssafy.client.user.config;

import com.ssafy.client.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestClientConfig {
	@Bean
	public RestTemplate restTemplate(OAuth2Interceptor oAuth2Interceptor) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(oAuth2Interceptor));
		return restTemplate;
	}
	@Bean
	public OAuth2Interceptor customInterceptor() {
		return new OAuth2Interceptor();
	}

	@Bean
	public RestTemplate logoutRestTemplate(){
		return new RestTemplate();
	}

}

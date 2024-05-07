package com.ssafy.client.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ssafy.client.user.service.ApiService;
import com.ssafy.client.user.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

	@Autowired
	private final OAuth2AuthorizedClientService authorizedClientService;
	@Autowired
	private final RestTemplate restTemplate;

	@GetMapping("/check-auth")
	public String checkAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return "No authentication found.";
		} else {
			return "Authentication: " + authentication.toString() + "\nPrincipal: " + authentication.getPrincipal().toString();
		}
	}


	@GetMapping("/token")
	public String getToken(Authentication authentication) {
		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
			"client", authentication.getName());
		if (client == null) {
			return "No client data found.";
		}

		OAuth2AccessToken accessToken = client.getAccessToken();
		return "Access Token: " + accessToken.getTokenValue();
	}

}

package com.ssafy.authorization.config;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ssafy.authorization.member.model.domain.Client;
import com.ssafy.authorization.member.model.repository.ClientRepository;

@Configuration
public class OAuthClientConfig {

	@Bean
	CommandLineRunner run(ClientRepository repository) {
		return args -> {
			Client clientDetails = new Client();
			clientDetails.setClientId("oidc-client");
			clientDetails.setAuthorizationGrantTypes("authorization_code,refresh_token");
			clientDetails.setClientAuthenticationMethods("client_secret_basic");
			clientDetails.setClientIdIssuedAt(Instant.now());
			clientDetails.setClientName("oidc-client");
			clientDetails.setClientSecret("{noop}secret"); // {noop} for no encryption
			clientDetails.setClientSecretExpiresAt(Instant.MAX);
			clientDetails.setPostLogoutRedirectUris("http://127.0.0.1:8080/login/oauth2/code/oidc-client");
			clientDetails.setRedirectUris("http://127.0.0.1:8080/");
			clientDetails.setScopes("openid,profile");

			repository.save(clientDetails);
		};
	}
}
package com.ssafy.authorization.config;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.util.Assert;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2AuthorizationServiceImpl implements OAuth2AuthorizationService {
	private final RegisteredClientRepository registeredClientRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		OAuth2Token accessToken = authorization.getAccessToken().getToken();
		redisTemplate.opsForValue()
			.set("accessToken:" + accessToken.getTokenValue(), accessToken.getTokenValue(),
				accessToken.getExpiresAt().compareTo(Instant.now()), TimeUnit.SECONDS);
		OAuth2Token refreshToken = authorization.getRefreshToken().getToken();
		redisTemplate.opsForValue()
			.set("refreshToken:" + refreshToken.getTokenValue(), refreshToken.getTokenValue(),
				refreshToken.getExpiresAt().compareTo(Instant.now()), TimeUnit.SECONDS);
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		OAuth2Token accessToken = authorization.getAccessToken().getToken();
		redisTemplate.delete("accessToken:" + accessToken.getTokenValue());
		OAuth2Token refreshToken = authorization.getRefreshToken().getToken();
		redisTemplate.delete("refreshToken:" + refreshToken.getTokenValue());
	}

	@Override
	public OAuth2Authorization findById(String id) {
		Assert.hasText(id, "id cannot be empty");
		RegisteredClient rc = registeredClientRepository.findById(id);
		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(rc)
			.id(id);
		return builder.build();
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");

		return null;
	}
}

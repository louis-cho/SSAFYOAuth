package com.ssafy.authorization.config;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ssafy.authorization.redirect.repository.RedirectEntityRepository;
import com.ssafy.authorization.team.entity.DeveloperTeamEntity;
import com.ssafy.authorization.team.repository.DeveloperTeamRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {

	private final DeveloperTeamRepository developerTeamRepository;
	private final RedirectEntityRepository redirectEntityRepository;

	@Override
	public void save(RegisteredClient registeredClient) {
		Assert.notNull(registeredClient, "registered client cannot be null");
		DeveloperTeamEntity e = new DeveloperTeamEntity();
		e.setTeamName(registeredClient.getClientId());
		e.setServiceName(registeredClient.getClientName());
		e.setServiceKey(registeredClient.getClientSecret());
		developerTeamRepository.save(e);
	}

	@Override
	public RegisteredClient findById(String id) {
		Assert.hasText(id, "id cannot be empty");
		Optional<DeveloperTeamEntity> optional = developerTeamRepository.findById(Integer.parseInt(id));
		String[] scope = {"email", "studentId", "name", "track", "phoneNumber", "gender", "image"};
		return optional.map(e->{
			RegisteredClient.Builder builder = RegisteredClient.withId(id)
				.clientId(e.getTeamName().toString())
				.clientIdIssuedAt(e.getCreateDate().toInstant(ZoneOffset.UTC))
				.clientSecret(e.getServiceKey())
				.clientName(e.getServiceName())
				.clientAuthenticationMethods(clientAuthenticationMethods -> {
					clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
					clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
					clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_JWT);
					clientAuthenticationMethods.add(ClientAuthenticationMethod.PRIVATE_KEY_JWT);
					clientAuthenticationMethods.add(ClientAuthenticationMethod.NONE);
				})
				.authorizationGrantTypes(grantTypes -> {
					grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
					grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
					grantTypes.add(AuthorizationGrantType.DEVICE_CODE);
					grantTypes.add(AuthorizationGrantType.JWT_BEARER);
					grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
				})
				.redirectUris(uris -> {
					redirectEntityRepository.findAllByTeamId(e.getSeq()).forEach(uri -> {
						uris.add(uri.getRedirect());
					});
				})
				.scopes(scopes ->{
					Arrays.stream(scope).toList().forEach(s -> {scopes.add(s);});
				});
			return builder.build();
		}).orElse(null);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		Assert.hasText(clientId, "client id cannot be empty");
		DeveloperTeamEntity client = developerTeamRepository.findByTeamName(clientId);
		if(client == null) return null;

		String[] scope = {"email", "studentId", "name", "track", "phoneNumber", "gender", "image"};
		RegisteredClient registeredClient = RegisteredClient.withId(client.getSeq().toString())
				.clientId(client.getTeamName().toString())
				.clientIdIssuedAt(client.getCreateDate().toInstant(ZoneOffset.UTC))
				.clientSecret(client.getServiceKey())
				.clientName(client.getServiceName())
				.clientAuthenticationMethods(clientAuthenticationMethods -> {
					clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
					clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
					clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_JWT);
					clientAuthenticationMethods.add(ClientAuthenticationMethod.PRIVATE_KEY_JWT);
					clientAuthenticationMethods.add(ClientAuthenticationMethod.NONE);
				})
				.authorizationGrantTypes(grantTypes -> {
					grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
					grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
					grantTypes.add(AuthorizationGrantType.DEVICE_CODE);
					grantTypes.add(AuthorizationGrantType.JWT_BEARER);
					grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
				})
				.redirectUris(uris -> {
					redirectEntityRepository.findAllByTeamId(client.getSeq()).forEach(uri -> {
						uris.add(uri.getRedirect());
					});
				})
				.scopes(scopes ->{
					Arrays.stream(scope).toList().forEach(s -> {scopes.add(s);});
				}).build();


		return registeredClient;
	}
}

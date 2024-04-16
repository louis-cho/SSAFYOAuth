package com.ssafy.authorization.member.model.repository;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.member.model.domain.Client;
import com.ssafy.authorization.utils.ClientUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {
	private final ClientUtils clientUtils;
	private final ClientRepository clientRepository;
	@Override
	public void save(RegisteredClient registeredClient) {
		Client entity = clientUtils.toEntity(registeredClient);
		clientRepository.save(entity);
	}

	@Override
	public RegisteredClient findById(String id) {
		Client client = clientRepository.findById(id).orElseThrow();
		return clientUtils.toObject(client);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		Client client = clientRepository.findByClientId(clientId).orElseThrow();
		return clientUtils.toObject(client);
	}
}

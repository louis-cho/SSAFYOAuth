package com.ssafy.authorization.member.model.repository;

import java.util.List;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.member.model.domain.Client;
import com.ssafy.authorization.utils.ClientUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
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
		log.info("{} dididididididiid"    , id);
		Client client = clientRepository.findById(id).orElseThrow();
		return clientUtils.toObject(client);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		log.info("clientID !!! {} ", clientId);
		Client client = clientRepository.findClientByClientId(clientId).orElseThrow();
		log.info("clientID !! : {}     {} client URI {}", clientId,client.getClientId(), client.getRedirectUris());
		return clientUtils.toObject(client);
	}
}

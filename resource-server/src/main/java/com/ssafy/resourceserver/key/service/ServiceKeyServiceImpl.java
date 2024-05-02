package com.ssafy.resourceserver.key.service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.ssafy.resourceserver.key.model.ServiceKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.resourceserver.key.model.domain.ServiceKey;

@Service
public class ServiceKeyServiceImpl implements ServiceKeyService {

	@Autowired
	private ServiceKeyRepository serviceKeyRepository;

	private static final Random random = new Random();

	@Override
	public String createServiceKey(Long teamId, Long clientId) {
		// 클라이언트 요청 검증, 팀에 속해 있는 유저의 요청인지 확인
		if (!verifyClient(clientId, teamId)) {
			throw new IllegalArgumentException("팀원이 맞아?");
		}

		// 서비스 키 발급 시도
		ServiceKey serviceKey = new ServiceKey();
		serviceKey.setTeamId(teamId);
		serviceKey.setKey(generateUniqueServiceKey());
		serviceKeyRepository.save(serviceKey);

		// 생성된 서비스 키 반환
		return serviceKey.getKey();
	}

	@Override
	public String reCreateServiceKey(Long teamId, Long clientId) {
		// 클라이언트 요청 검증, 팀에 속해 있는 유저의 요청인지 확인
		if (!verifyClient(clientId, teamId)) {
			throw new IllegalArgumentException("팀원이 맞아?");
		}
		// 서비스 키 재발급
		Optional<ServiceKey> optionalServiceKey = serviceKeyRepository.findById(teamId);
		if (optionalServiceKey.isPresent()) {
			ServiceKey serviceKey = optionalServiceKey.get();
			serviceKey.setKey(generateUniqueServiceKey());
			serviceKeyRepository.save(serviceKey);
			return serviceKey.getKey();
		} else {
			throw new IllegalStateException("팀이 없어ㅜ");
		}
	}


	private boolean verifyClient(Long teamId, Long clientId) {
		// 예시로 단순히 clientId와 requestBodyHash를 비교하는 것으로 대체
		return clientId.equals(teamId);
	}

	private String generateUniqueServiceKey() {
		String newServiceKey;
		boolean isKeyUnique;

		do {
			newServiceKey = UUID.randomUUID().toString();
			isKeyUnique = isServiceKeyUnique(newServiceKey);
		} while (!isKeyUnique);

		return newServiceKey;
	}

	private boolean isServiceKeyUnique(String key) {
		return !serviceKeyRepository.existsByKey(key);
	}


}

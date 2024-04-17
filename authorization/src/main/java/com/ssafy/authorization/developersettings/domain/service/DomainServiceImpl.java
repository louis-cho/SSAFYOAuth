package com.ssafy.authorization.developersettings.domain.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.authorization.developersettings.domain.model.DomainEntity;
import com.ssafy.authorization.developersettings.domain.repository.DomainEntityRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DomainServiceImpl implements DomainService {

	private final DomainEntityRepository domainEntityRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	DomainServiceImpl(DomainEntityRepository domainEntityRepository) {
		this.domainEntityRepository = domainEntityRepository;
	}

	@Override
	@Transactional
	public int saveDomain(DomainEntity domain) {
		UUID teamId = domain.getTeamId();
		UUID userId = domain.getUserId();
		String domainUrl = domain.getDomain();

		if (!isTeamMember(teamId, userId)) {
			// 팀원이 아닌 경우
			return -3;
		}

		// 2. 해당 팀의 사이트 도메인 URL 개수 확인 및 추가
		return addDomainUrl(teamId, userId, domainUrl);
	}

	private boolean isTeamMember(UUID teamId, UUID userId) {
		// ToDO: 팀원 판별 기능 호출하기
		return true; // 임시로 true 반환
	}

	@Override
	public int countDomainUrl(UUID teamId) {
		return domainEntityRepository.countByTeamId(teamId);
	}

	private int countRedirectUrl(UUID teamId) {
		return 0;
	}

	private int addDomainUrl(UUID teamId, UUID userId, String domainUrl) {
		try {
			int count = countDomainUrl(teamId);
			// 도메인 URL 개수가 6개 미만인 경우에만 URL 추가
			if (count < 6) {
				DomainEntity domainEntity = new DomainEntity(teamId, userId, domainUrl);
				domainEntityRepository.save(domainEntity);
			}
		} catch (Exception e) {
			return -1;
		}

		return 1;
	}
}

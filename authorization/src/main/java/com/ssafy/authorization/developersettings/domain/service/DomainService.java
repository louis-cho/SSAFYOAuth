package com.ssafy.authorization.developersettings.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.authorization.developersettings.domain.model.DomainEntity;
import com.ssafy.authorization.developersettings.domain.repository.DomainEntityRepository;

@Service
public class DomainService {

	private final DomainEntityRepository domainRepository;

	@Autowired
	public DomainService(DomainEntityRepository domainRepository) {
		this.domainRepository = domainRepository;
	}

	public void saveDomain(DomainEntity domain) {
		domainRepository.save(domain);
	}
}

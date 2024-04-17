package com.ssafy.authorization.developersettings.domain.service;

import org.springframework.stereotype.Service;

import com.ssafy.authorization.developersettings.domain.model.DomainEntity;

@Service
public interface DomainService {

	int saveDomain(DomainEntity domainEntity);
}

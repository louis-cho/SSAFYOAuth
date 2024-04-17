package com.ssafy.authorization.developersettings.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ssafy.authorization.developersettings.domain.model.DomainEntity;

@Service
public interface DomainService {

	int saveDomain(DomainEntity domainEntity);

	int countDomainUrl(UUID teamId);
}

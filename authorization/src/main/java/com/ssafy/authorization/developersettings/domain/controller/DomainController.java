package com.ssafy.authorization.developersettings.domain.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.authorization.developersettings.domain.model.DomainEntity;
import com.ssafy.authorization.developersettings.domain.repository.DomainEntityRepository;
import com.ssafy.authorization.developersettings.domain.service.DomainService;

@RestController
@RequestMapping("/api/team/domain")
public class DomainController {

	DomainService domainService;
	DomainEntityRepository domainEntityRepository;

	@Autowired
	DomainController(DomainService domainService, DomainEntityRepository domainEntityRepository) {
		this.domainService = domainService;
		this.domainEntityRepository = domainEntityRepository;
	}

	private DomainEntity parseDomainEntity(JsonNode jsonNode) {
		JsonNode domainNode = null, teamNode = null, userNode = null;
		String domain;
		UUID teamId, userId;

		domainNode = jsonNode.get("domainUrl");
		teamNode = jsonNode.get("teamId");
		userNode = jsonNode.get("userId");

		if (domainNode == null || teamNode == null || userNode == null) {
			return null;
		}
		domain = domainNode.asText();
		try {
			teamId = UUID.fromString(teamNode.asText());
			userId = UUID.fromString(userNode.asText());
		} catch (IllegalArgumentException e) {
			return null;
		}

		return new DomainEntity(teamId, userId, domain);
	}

	@PostMapping("/regist")
	public int registDomain(@RequestBody JsonNode requestBody) {
		DomainEntity domainEntity = parseDomainEntity(requestBody);
		if (domainEntity == null) {
			return -1;
		}
		return domainService.insertDomain(domainEntity);
	}

	@PostMapping("/delete")
	public int deleteDomain(@RequestBody JsonNode requestBody) {
		DomainEntity domainEntity = parseDomainEntity(requestBody);
		if (domainEntity == null) {
			return -1;
		}
		return domainService.removeDomain(domainEntity);
	}
}

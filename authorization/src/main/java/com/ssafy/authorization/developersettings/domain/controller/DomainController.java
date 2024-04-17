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

	@PostMapping("/test")
	public void regist() {
		String domainDomain;
		UUID teamId, userId;
		domainDomain = "http://127.43.42.1/test";
		teamId = UUID.randomUUID();
		userId = UUID.randomUUID();

		DomainEntity entity = new DomainEntity(teamId, userId, domainDomain);
		domainEntityRepository.save(entity);
	}

	@PostMapping("/regist")
	public int registDomain(@RequestBody JsonNode requestBody) {

		JsonNode domainNode, teamNode, userNode;
		String domain;
		UUID teamId, userId;

		domainNode = requestBody.get("domainUrl");
		teamNode = requestBody.get("teamId");
		userNode = requestBody.get("userId");

		if (domainNode == null || teamNode == null || userNode == null) {
			// Todo : Json 데이터 형식 에러 리턴
			return -1;
		} else {
			try {
				domain = domainNode.asText();
				teamId = UUID.fromString(teamNode.asText());
				userId = UUID.fromString(userNode.asText());
			} catch (NullPointerException e) {
				// 에러 코드 응답
				// Todo : Json 데이터 형식 에러 리턴
				return -1;
			}
		}

		DomainEntity domainEntity = new DomainEntity(teamId, userId, domain);

		return domainService.saveDomain(domainEntity);
	}
}

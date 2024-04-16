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
public class DomainUrlController {

	DomainService domainService;
	DomainEntityRepository domainEntityRepository;

	@Autowired
	DomainUrlController(DomainService domainService, DomainEntityRepository domainEntityRepository) {
		this.domainService = domainService;
		this.domainEntityRepository = domainEntityRepository;
	}

	@PostMapping("/test")
	public void regist() {
		String domainUrl;
		UUID teamId, userId;
		domainUrl = "http://127.43.42.1/test";
		teamId = UUID.randomUUID();
		userId = UUID.randomUUID();

		DomainEntity entity = new DomainEntity(teamId, userId, domainUrl);
		domainEntityRepository.save(entity);
	}

	@PostMapping("/regist")
	public String registDomain(@RequestBody JsonNode requestBody) {

		JsonNode domainNode, teamNode, userNode;
		String domainUrl, teamId, userId;

		domainNode = requestBody.get("domainUrl");
		teamNode = requestBody.get("teamId");
		userNode = requestBody.get("userId");

		if (domainNode == null || teamNode == null || userNode == null) {
			return "X";
		} else {
			try {
				domainUrl = domainNode.asText();
				teamId = teamNode.asText();
				userId = userNode.asText();
			} catch (NullPointerException e) {
				// 에러 코드 응답
				return "X";
			}
		}

		return "hi";
	}
}

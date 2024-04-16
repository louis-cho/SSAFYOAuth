package com.ssafy.authorization.developersettings.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/team/domain")
public class DomainUrlController {

	@GetMapping("/regist")
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

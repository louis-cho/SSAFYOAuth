package com.ssafy.authorization.stats.login.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.authorization.stats.login.model.LoginDocument;
import com.ssafy.authorization.stats.login.service.LoginStatsService;

@RestController
@RequestMapping("/api/ttt")
public class LoginStatsTestController {

	LoginStatsService loginStatsService;

	@Autowired
	LoginStatsTestController(LoginStatsService loginStatsService) {
		this.loginStatsService = loginStatsService;
	}

	@PostMapping("/test")
	public int testLogin(@RequestBody JsonNode requestBody) {
		UUID temaId = UUID.fromString(requestBody.get("teamId").asText());
		UUID userId = UUID.fromString(requestBody.get("userId").asText());

		Instant createdAt = Instant.now();

		LoginDocument loginDocument = new LoginDocument(temaId, userId, createdAt);
		return loginStatsService.saveUserLogin(loginDocument);

	}
}

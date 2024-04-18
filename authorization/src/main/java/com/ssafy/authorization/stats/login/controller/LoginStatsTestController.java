package com.ssafy.authorization.stats.login.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.authorization.stats.login.model.LoginStats;
import com.ssafy.authorization.stats.login.service.LoginStatsService;

@RestController
@RequestMapping("/api/ttt")
public class LoginStatsTestController {

	LoginStatsService loginStatsService;

	@Autowired
	LoginStatsTestController(LoginStatsService loginStatsService) {
		this.loginStatsService = loginStatsService;
	}

	@PostMapping("/save")
	public String testSave(@RequestBody JsonNode requestBody) {
		UUID temaId = UUID.fromString(requestBody.get("teamId").asText());
		UUID userId = UUID.fromString(requestBody.get("userId").asText());

		Instant createdAt = Instant.now();

		LoginStats loginStats = new LoginStats(temaId.toString(), userId.toString(), createdAt);
		loginStatsService.save(loginStats);

		return "finished";

	}

	@PostMapping("/fetch")
	public String testFetch(@RequestBody JsonNode requestBody) {
		String temaId = requestBody.get("teamId").asText();
		String userId = requestBody.get("userId").asText();

		Instant createdAt = Instant.now();
		List<LoginStats> list = null;
		try {
			list = loginStatsService.fetch(userId, temaId);
		} catch (Exception e) {
			return null;
		}
		StringBuilder ret = new StringBuilder();
		for (LoginStats loginStats : list) {
			ret.append(loginStats.toString());
		}
		return ret.toString();

	}

}

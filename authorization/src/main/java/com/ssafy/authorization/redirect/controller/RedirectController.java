package com.ssafy.authorization.redirect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.authorization.redirect.model.RedirectEntity;
import com.ssafy.authorization.redirect.repository.RedirectEntityRepository;
import com.ssafy.authorization.redirect.service.RedirectService;

@RestController
@RequestMapping("/api/team/redirect")
public class RedirectController {

	RedirectService redirectService;
	RedirectEntityRepository redirectEntityRepository;

	@Autowired
	RedirectController(RedirectService redirectService, RedirectEntityRepository redirectEntityRepository) {
		this.redirectService = redirectService;
		this.redirectEntityRepository = redirectEntityRepository;
	}

	private RedirectEntity parseRedirectEntity(JsonNode jsonNode) {
		JsonNode redirectNode = null, teamNode = null, userNode = null;
		String redirect;
		int teamId, userId;

		redirectNode = jsonNode.get("redirectUrl");
		teamNode = jsonNode.get("teamId");
		userNode = jsonNode.get("userId");

		if (redirectNode == null || teamNode == null || userNode == null) {
			return null;
		}
		redirect = redirectNode.asText();
		try {
			teamId = Integer.parseInt(teamNode.asText());
			userId = Integer.parseInt(userNode.asText());
		} catch (IllegalArgumentException e) {
			return null;
		}

		return new RedirectEntity(teamId, userId, redirect);
	}

	@PostMapping("/regist")
	public int registRedirect(@RequestBody JsonNode requestBody) {
		RedirectEntity redirectEntity = parseRedirectEntity(requestBody);
		if (redirectEntity == null) {
			return -1;
		}
		return redirectService.insertRedirect(redirectEntity);
	}

	@PostMapping("/delete")
	public int deleteRedirect(@RequestBody JsonNode requestBody) {
		RedirectEntity redirectEntity = parseRedirectEntity(requestBody);
		if (redirectEntity == null) {
			return -1;
		}
		return redirectService.removeRedirect(redirectEntity);
	}
}

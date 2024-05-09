package com.ssafy.resourceserver.redirect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.resourceserver.member.model.repository.MemberRepository;
import com.ssafy.resourceserver.redirect.model.RedirectEntity;
import com.ssafy.resourceserver.redirect.repository.RedirectEntityRepository;
import com.ssafy.resourceserver.redirect.service.RedirectService;

@RestController
@RequestMapping("/api/team/redirect")
public class RedirectController {

	RedirectService redirectService;
	RedirectEntityRepository redirectEntityRepository;
	MemberRepository memberRepository;

	@Autowired
	RedirectController(RedirectService redirectService, RedirectEntityRepository redirectEntityRepository, MemberRepository memberRepository) {
		this.redirectService = redirectService;
		this.redirectEntityRepository = redirectEntityRepository;
		this.memberRepository = memberRepository;
	}

	private RedirectEntity parseRedirectEntity(JsonNode jsonNode, Jwt jwt) {
		JsonNode redirectNode = null, teamNode = null;
		String redirect;
		int teamId, userId;

		redirectNode = jsonNode.get("redirectUrl");
		teamNode = jsonNode.get("teamId");

		if (redirectNode == null || teamNode == null) {
			return null;
		}
		redirect = redirectNode.asText();
		try {
			teamId = Integer.parseInt(teamNode.asText());
			String userEmail = jwt.getClaimAsString("sub");
			userId = memberRepository.findByEmail(userEmail).get().getMemberId();
		} catch (IllegalArgumentException e) {
			return null;
		}

		return new RedirectEntity(teamId, userId, redirect);
	}

	@PostMapping("/regist")
	public int registRedirect(@RequestBody JsonNode requestBody, @AuthenticationPrincipal Jwt jwt) {
		RedirectEntity redirectEntity = parseRedirectEntity(requestBody, jwt);
		if (redirectEntity == null) {
			return -1;
		}
		return redirectService.insertRedirect(redirectEntity);
	}

	@PostMapping("/delete")
	public int deleteRedirect(@RequestBody JsonNode requestBody, @AuthenticationPrincipal Jwt jwt) {
		RedirectEntity redirectEntity = parseRedirectEntity(requestBody, jwt);
		if (redirectEntity == null) {
			return -1;
		}
		return redirectService.removeRedirect(redirectEntity);
	}
}

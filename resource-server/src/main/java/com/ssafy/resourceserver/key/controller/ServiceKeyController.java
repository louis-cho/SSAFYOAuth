package com.ssafy.resourceserver.key.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ssafy.resourceserver.key.service.ServiceKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/team/key")
@RequiredArgsConstructor
@Slf4j
public class ServiceKeyController {

	@Autowired
	private final ServiceKeyService serviceKeyService;

	@PostMapping("/claim/{teamid}")
	public ResponseEntity<?> claim(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer teamid){
		serviceKeyService.createServiceKey(teamid, jwt.getClaimAsString("sub"));
		return ResponseEntity.ok(Collections.singletonMap("result", true));
	}


	@PostMapping("/reclaim/{teamid}")
	public ResponseEntity<?> reclaim(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer teamid){
		serviceKeyService.reCreateServiceKey(teamid, jwt.getClaimAsString("sub"));
		return ResponseEntity.ok(Collections.singletonMap("result", true));
	}
}

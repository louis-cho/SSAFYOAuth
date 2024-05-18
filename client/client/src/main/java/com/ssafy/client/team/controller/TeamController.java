package com.ssafy.client.team.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.client.user.service.ApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.client.user.service.ApiService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
	private static final String RESOURCE_SERVER_URL = "http://localhost:8090/api/";
	private final ApiService apiService;
	private final ObjectMapper objectMapper;
	private final String SUFFIX = "pages/";

	@PostMapping("/{teamSeq}/country-ip")
	public ResponseEntity<?> blocked(@PathVariable String teamSeq, @RequestBody List<String> blockedCountries,
		Model model) {
		log.info("teamSeq : {}, blockedCountries : {}", teamSeq, blockedCountries);
		String url = RESOURCE_SERVER_URL + "team/" + teamSeq + "/country-ip";
		String result = apiService.callPostCountry(url, blockedCountries);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@GetMapping("/detail/{team-seq}")
	public String teamDetail(@PathVariable("team-seq") Integer teamSeq, Model model) {
		model.addAttribute("teamSeq", teamSeq);
		return "team/detail";
	}

	@GetMapping("/{teamSeq}/login-log")
	public String loginLog(@PathVariable Integer teamSeq) {

		return SUFFIX+ "login-log";
	}
}

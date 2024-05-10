package com.ssafy.client.team.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	private final ApiService apiService;

	@PostMapping("/country-ip/blocked")
	public ResponseEntity<?> blocked(){
		log.info("rest api blocked");

		return ResponseEntity.status(HttpStatus.OK).build();
	}
	@GetMapping("/detail/{team-seq}")
	public String teamDetail(@PathVariable("team-seq") Integer teamSeq, Model model) {
		model.addAttribute("teamSeq", teamSeq);
		return "team/detail";
	}

}

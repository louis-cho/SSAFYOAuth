package com.ssafy.client.team.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.client.user.service.ApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TeamController {

	private final ApiService apiService;

	@PostMapping("/country-ip/blocked")
	public ResponseEntity<?> blocked(){
		log.info("rest api blocked");

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}

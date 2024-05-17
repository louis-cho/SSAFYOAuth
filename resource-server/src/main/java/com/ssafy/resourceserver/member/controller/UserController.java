package com.ssafy.resourceserver.member.controller;

import com.ssafy.resourceserver.member.TempDto;
import com.ssafy.resourceserver.member.model.dto.ProfileInformationForUpdatesDto;
import com.ssafy.resourceserver.member.model.dto.UserInfo;
import com.ssafy.resourceserver.member.service.MemberService;
import com.ssafy.resourceserver.team.service.DeveloperService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final MemberService memberService;
	private final DeveloperService developerService;
	@PostMapping("/developer-sign-up")
	public ResponseEntity<?> developerSignUp(@RequestBody Integer seq) {
		log.info("들어온 인티저 : {} ", seq);
		developerService.developerSignUp(seq);
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	@GetMapping("/check")
	public ResponseEntity<?> check(@AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		log.info("들어온 email value : {} ", email);
		TempDto result = memberService.checkUser(email);
		log.info("result ttt : {}", result);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	// @PostMapping("/sign-up")
	// public ResponseEntity<?> signUp(@RequestBody Integer seq, @AuthenticationPrincipal Jwt jwt) {
	// 	log.info("before");
	// 	memberService.signUp(seq);
	// 	log.info("after");
	// 	return ResponseEntity.status(HttpStatus.OK).body(true);
	// }

	@GetMapping("/info")
	public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
		// Jwt에서 사용자 정보 추출
		log.info("test : {} ", jwt.getTokenValue());
		log.info("{} JWT TTT", jwt);
		String email = jwt.getClaimAsString("sub");
		List<String> scopes = jwt.getClaimAsStringList("scope");

		Map<String, Object> userProfile = memberService.getUserProfile(email, scopes);
		log.info("유저정보 스코프별: {} ", userProfile);
		return userProfile;
	}

	@GetMapping("/data")
	@ResponseBody
	public ResponseEntity<?> getUpdateForUserData(@AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		ProfileInformationForUpdatesDto dto = memberService.ProfileInforForUpdatesData(email);
		return ResponseEntity.ok(dto);
	}

	@PostMapping("/info")
	public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal Jwt jwt,
		@ModelAttribute ProfileInformationForUpdatesDto userDto) {
		// Jwt에서 사용자 정보 추출
		log.info("test : {} ", jwt.getTokenValue());
		log.info("{} JWT TTT", jwt);
		String email = jwt.getClaimAsString("sub");

		try {
			memberService.updateUserProfile(email, userDto);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/info/grade")
	public ResponseEntity<?> updateSecurityGrade(@AuthenticationPrincipal Jwt jwt, @RequestBody Integer grade) {
		// Jwt에서 사용자 정보 추출
		log.info("test : {} ", jwt.getTokenValue());
		log.info("{} JWT TTT", jwt);
		String email = jwt.getClaimAsString("sub");

		try {
			memberService.updateSecurityGrade(email, grade);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}

	@PostMapping("/info/password")
	public ResponseEntity<?> updatePassword(@AuthenticationPrincipal Jwt jwt,
		@RequestBody Map<String, String> passwords) {
		// Jwt에서 사용자 정보 추출
		log.info("test : {} ", jwt.getTokenValue());
		log.info("{} JWT TTT", jwt);
		String email = jwt.getClaimAsString("sub");

		try {
			memberService.updatePassword(email, passwords);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}

	@GetMapping("/test")
	public String test(@AuthenticationPrincipal Jwt jwt) {
		// Jwt에서 사용자 정보 추출
		log.info("test : {} ", jwt.getTokenValue());
		String username = jwt.getClaimAsString("sub");
		String scope = jwt.getClaimAsString("scope");
		log.info("test {} {}", username, scope);

		return "잘되네";
	}

}
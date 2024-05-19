package com.ssafy.resourceserver.team.controller;

import java.util.Map;

import com.ssafy.resourceserver.team.model.domain.SignUpDevelopDto;
import com.ssafy.resourceserver.team.service.DeveloperService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class DevelopController {

	private final DeveloperService developerService;

	@GetMapping("/team")
	public String team(){
		return "login";
	}


	@GetMapping("/change/{userId}")
	public String developChange(@Valid @PathVariable Long userId, BindingResult bindingResult, Model model) {
		log.info("userId ttttt= {}", userId);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errMessage", bindingResult.getAllErrors());
			return "develop_sign_up";
		} else {
			return "develop_main";
		}
	}

	@GetMapping("/sign-up")
	public String developPost(@Valid @ModelAttribute SignUpDevelopDto signUpDevelopDto, BindingResult bindingResult, Model model) {
		log.info("signUpDevelopDto = {}", signUpDevelopDto);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errMessage", bindingResult.getAllErrors());
			return "develop_sign_up";
		} else {
			return "develop_main";
		}
	}

	@PostMapping("/developer-sign-up")
	public ResponseEntity<?> developerSignUp(@RequestBody Map<String, Integer> body) {
		developerService.developerSignUp(body.get("memberSeq"));
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}

}

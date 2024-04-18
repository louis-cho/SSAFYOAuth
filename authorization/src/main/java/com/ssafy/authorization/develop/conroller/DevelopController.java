package com.ssafy.authorization.develop.conroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.authorization.develop.model.domain.SignUpDevelopDto;
import com.ssafy.authorization.member.model.dto.SignUpRequestDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/develop")
public class DevelopController {
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

}

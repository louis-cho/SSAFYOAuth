package org.example.authorization.member.controller;

import org.example.authorization.member.model.dto.SignUpRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MemberController {
	@GetMapping("/sign_up")
	public String signUp() {
		return "sign_up";
	}

	@PostMapping("/sign_up")
	public String signUpPost(@Valid @ModelAttribute SignUpRequestDto dto, BindingResult bindingResult, Model model) {
		log.info("Post Data : {} ", dto);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errMessage", bindingResult.getAllErrors());
			return "sign_up";
		}
		else{
			return "login";
		}
	}

}

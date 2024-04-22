package com.ssafy.authorization.member.controller;

import com.ssafy.authorization.member.model.domain.Member;
import com.ssafy.authorization.member.model.dto.SignUpRequestDto;
import com.ssafy.authorization.member.model.service.CustomMemberManager;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
	private final CustomMemberManager customMemberManager;
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
		} else {
			Member member = Member.create(dto.getUserEmail(), dto.getPassword());
			customMemberManager.createUser(member);
			return "login";
		}
	}

}

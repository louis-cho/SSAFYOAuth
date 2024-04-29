package com.ssafy.authorization.member.controller;

import java.util.HashMap;
import java.util.Map;

import com.ssafy.authorization.mail.service.EmailService;
import com.ssafy.authorization.member.model.domain.Member;
import com.ssafy.authorization.member.model.dto.ResetPasswordDto;
import com.ssafy.authorization.member.model.dto.SignUpRequestDto;
import com.ssafy.authorization.member.model.service.CustomMemberManager;
import com.ssafy.authorization.member.model.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	private final EmailService emailService;

	private final CustomMemberManager customMemberManager;

	@GetMapping("/signup")
	public String signUp() {
		return "signup";
	}

	@PostMapping("/signup")
	public void sign_Up(@ModelAttribute SignUpRequestDto signUpRequestDto) {
		System.out.println(signUpRequestDto);
		memberService.save(Member.create(signUpRequestDto), signUpRequestDto);
	}

	@PostMapping("/sendemail")
	public void sendEmail(@RequestBody String userEmail) throws Exception {
		log.info("{} tttttttttttt",userEmail);
		emailService.sendEmail(userEmail);
	}

	@PostMapping("/certify")
	public ResponseEntity<Map<String, Boolean>> certify(@RequestBody Map<String,String> requestBody) throws Exception {
		boolean result = emailService.certify(requestBody.get("userEmail"), requestBody.get("userCode"));
		Map<String, Boolean> response = new HashMap<>();
		response.put("result", result);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/forgot_password")
	public String forgotPassword(){
		return "forgot_password";
	}

	//  @Valid 추가해야함
	@PostMapping("/reset_password")
	public void resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
		customMemberManager.changePassword(resetPasswordDto.getOldPassword(), resetPasswordDto.getNewPassword());
		log.info("{} 비밀번호 바꾸기 성공4386731268579047945268754829624786584732958230475098243");
	}

	// @PostMapping("/sign_up")
	// public String signUpPost(@Valid @ModelAttribute SignUpRequestDto dto, BindingResult bindingResult, Model model) {
	// 	log.info("Post Data : {} ", dto);
	// 	if (bindingResult.hasErrors()) {
	// 		model.addAttribute("errMessage", bindingResult.getFieldError());
	// 		return "sign_up";
	// 	} else {
	// 		return "tmp_login";
	// 	}
	// }

// 	@GetMapping("/login")
// 	public String login(){
// 		return "login";
// 	}
}

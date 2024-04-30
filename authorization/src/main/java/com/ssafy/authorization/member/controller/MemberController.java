package com.ssafy.authorization.member.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssafy.authorization.mail.service.EmailService;
import com.ssafy.authorization.member.model.domain.Member;
import com.ssafy.authorization.member.model.dto.FindUserEmailDto;
import com.ssafy.authorization.member.model.dto.ResetPasswordDto;
import com.ssafy.authorization.member.model.dto.SignUpRequestDto;
import com.ssafy.authorization.member.model.service.CustomMemberManager;
import com.ssafy.authorization.member.model.service.MemberService;
import com.ssafy.authorization.team.service.TeamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	private final EmailService emailService;

	private final CustomMemberManager customMemberManager;

	private final TeamService teamService;

	@GetMapping("/")
	public String thisdfz(Model model, Authentication authentication) {
		Map data = teamService.listTeam(authentication);
		System.out.println(data);
		List<String> teamNames = new ArrayList<>();
		teamNames.add("루피");
		teamNames.add("망곰이");
		model.addAttribute("teamNames", teamNames);
		return "index";
	}

	@GetMapping("/signup")
	public String signUp() {
		return "signup";
	}

	@PostMapping("/signup")
	public String sign_Up(@ModelAttribute @Valid SignUpRequestDto signUpRequestDto, BindingResult result, Model model) {
		System.out.println(signUpRequestDto);
		if (result.hasErrors()) {
			model.addAttribute("errors", result.getAllErrors());
			return "signup";
		}
		memberService.save(Member.create(signUpRequestDto), signUpRequestDto);
		return "login";
	}

	@PostMapping("/sendemail")
	public void sendEmail(@RequestBody String userEmail) throws Exception {
		log.info("{} tttttttttttt", userEmail);
		emailService.sendEmail(userEmail);
	}

	@PostMapping("/certify")
	public ResponseEntity<Map<String, Boolean>> certify(@RequestBody Map<String, String> requestBody) throws Exception {
		boolean result = emailService.certify(requestBody.get("userEmail"), requestBody.get("userCode"));
		Map<String, Boolean> response = new HashMap<>();
		response.put("result", result);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/forgot_password")
	public String forgotPassword() {
		return "forgot_password";
	}

	//  @Valid 추가해야함
	@PostMapping("/reset_password")
	public String resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
		customMemberManager.changePassword(resetPasswordDto.getOldPassword(), resetPasswordDto.getNewPassword());
		log.info("{} 비밀번호 바꾸기 성공4386731268579047945268754829624786584732958230475098243");
		return "login";
	}

	@GetMapping("/forgot_user")
	public String forgetUser() {
		return "forgot_user";
	}

	@PostMapping("/find_user")
	@ResponseBody // 이 어노테이션을 추가하여 응답 본문이 JSON임을 명시합니다.
	public ResponseEntity<?> findUser(@RequestBody FindUserEmailDto findUserDto) {
		log.info("test find_user {}", findUserDto);
		String memberEmail = memberService.findMemberEmail(findUserDto);
		if (memberEmail != null) {
			// 사용자 이메일을 JSON 객체로 반환
			return ResponseEntity.ok(Collections.singletonMap("email", memberEmail));
		} else {
			// 사용자를 찾을 수 없는 경우 에러 메시지를 반환
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Collections.singletonMap("error", "사용자를 찾을 수 없습니다."));
		}
	}
}

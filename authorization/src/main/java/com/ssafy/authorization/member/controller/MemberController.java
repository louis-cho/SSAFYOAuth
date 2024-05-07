package com.ssafy.authorization.member.controller;

import com.ssafy.authorization.mail.service.EmailService;
import com.ssafy.authorization.member.model.domain.Member;
import com.ssafy.authorization.member.model.dto.FindUserEmailDto;
import com.ssafy.authorization.member.model.dto.ResetPasswordDto;
import com.ssafy.authorization.member.model.dto.SignUpRequestDto;
import com.ssafy.authorization.member.model.dto.UserEmailDto;
import com.ssafy.authorization.member.model.service.CustomMemberManager;
import com.ssafy.authorization.member.model.service.MemberFacade;
import com.ssafy.authorization.member.model.service.MemberService;

import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	private final MemberFacade memberFacade;

	private final EmailService emailService;

	private final CustomMemberManager customMemberManager;

	// @GetMapping("/")
	// public String thisdfz(Model model, Authentication authentication) {
	// 	Map data = teamService.listTeam(authentication);
	// 	System.out.println(data);
	// 	List<String> teamNames = new ArrayList<>();
	// 	teamNames.add("루피");
	// 	teamNames.add("망곰이");
	// 	model.addAttribute("teamNames", teamNames);
	// 	return "index";
	// }

	@GetMapping("/signup")
	public String signUp() {
		log.debug("signup 실행됨");
		return "signup";
	}


	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @ModelAttribute SignUpRequestDto signUpRequestDto, BindingResult result) {
		Map<String, String> fieldErrors = new HashMap<>();
		if (result.hasErrors()) {
			for (FieldError error : result.getFieldErrors()) {
				fieldErrors.put(error.getField(), error.getDefaultMessage());
			}
			log.info("test {}", fieldErrors);
			// 유효성 검사 실패 응답으로 400 상태 코드와 함께 에러 메시지를 반환
			return ResponseEntity.badRequest().body(fieldErrors);
		}
		boolean exists = customMemberManager.userExists(signUpRequestDto.getUserEmail());
		if(exists){
			fieldErrors.put("email","이미 존재하는 유저입니다.");
			return ResponseEntity.badRequest().body(fieldErrors);
		}
		memberService.save(Member.create(signUpRequestDto), signUpRequestDto);
		return ResponseEntity.ok("ok");
	}



	@PostMapping("/sendemail")
	public ResponseEntity<?> sendEmail(@RequestBody String userEmail) throws Exception {
		boolean exists = customMemberManager.userExists(userEmail);
		if(exists){
			Map<String, String> fieldErrors = new HashMap<>();
			fieldErrors.put("email","이미 존재하는 유저입니다.");
			return ResponseEntity.badRequest().body(fieldErrors);
		}
		emailService.sendEmail(userEmail);
		return ResponseEntity.ok("ok");
	}

	@PostMapping("/certify")
	public ResponseEntity<Map<String, Boolean>> certify(@RequestBody Map<String, String> requestBody) throws Exception {
		boolean result = emailService.certify(requestBody.get("userEmail"), requestBody.get("userCode"));
		Map<String, Boolean> response = new HashMap<>();
		response.put("result", result);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/reset_password")
	public String resetPassword() {
		return "reset_password";
	}


	@PostMapping("/reset_password")
	public String resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) { 	//  @Valid 추가해야함
		customMemberManager.changePassword(resetPasswordDto.getOldPassword(), resetPasswordDto.getNewPassword());
		log.info("{} 비밀번호 바꾸기 성공4386731268579047945268754829624786584732958230475098243");
		return "login";
	}

	@GetMapping("/forgot_password")
	public String forgotPassword() {
		return "forgot_password";
	}

	@PostMapping("/forgot_password")
	public ResponseEntity<?> forgotPassword(@RequestBody UserEmailDto userEmailDto) {
		Map<String, Boolean> response = memberFacade.forgotPassword(userEmailDto.getUserEmail());
		return ResponseEntity.ok().body(response);
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

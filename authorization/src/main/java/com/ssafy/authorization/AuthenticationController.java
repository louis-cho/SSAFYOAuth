package com.ssafy.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ssafy.authorization.mail.service.EmailService;

@Controller
public class AuthenticationController {

	@Autowired
	EmailService emailService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/test")
	public String test() throws Exception {
		// emailService.sendEmail("kdn1030@naver.com");
		return "dana";
	}

	@GetMapping("/test2/{userCode}")
	public String test2(@PathVariable String userCode) throws Exception {
		System.out.println(emailService.certify(userCode));
		return "dana";
	}

}

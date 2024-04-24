package com.ssafy.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ssafy.authorization.mail.service.EmailService;

@Controller
public class AuthenticationController {

	@Autowired
	EmailService emailService;

	@GetMapping("/login")
	public String login() {
		return "tmp_login";
	}
}

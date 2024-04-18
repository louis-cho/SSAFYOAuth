package com.ssafy.authorization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// @RequestMapping("test")
public class TestController {
	@GetMapping("/temp")
	public String temp() {
		return "temp";
	}


	@GetMapping("/signup")
	public String test() throws Exception {
		// emailService.sendEmail("kdn1030@naver.com");
		return "register";
	}
}


package com.ssafy.authorization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {
	@GetMapping("/temp")
	public String temp() {
		return "login";
	}


	@GetMapping("/signup")
	public String test() throws Exception {
		// emailService.sendEmail("kdn1030@naver.com");
		return "signup";
	}
	@GetMapping("/my_team_member")
	public String my_team_member() throws Exception {
		// emailService.sendEmail("kdn1030@naver.com");
		return "my_team_member";
	}

	@GetMapping("/main")
	public String main(){
		return "index";
	}

	@GetMapping("/myteaminfopage")
	public String myteaminfopage() {
		return "myteaminfopage";
	}
	@GetMapping("/my_team_agree")
	public String my_team_agree(){
		return "my_team_agree";
	}

	@GetMapping("/login")
	public String login(){
		return "login";
	}

	@GetMapping("/forgot_password")
	public String forgot_password(){
		return "reset_password";
	}
}
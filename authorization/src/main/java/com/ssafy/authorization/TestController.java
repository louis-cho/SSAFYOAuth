package com.ssafy.authorization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {
	@PostMapping("/temp")
	public String temp() {
		return "temp";
	}
}

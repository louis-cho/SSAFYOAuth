package com.ssafy.client.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PageController {

	@GetMapping("/team")
	public String teamMember(){
		return "management";
	}


	@GetMapping("/ip")
	public String ip(){
		return "country-ip";
	}

}

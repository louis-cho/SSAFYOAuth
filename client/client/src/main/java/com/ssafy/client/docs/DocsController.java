package com.ssafy.client.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class DocsController {


	@GetMapping("/team")
	public String apiTeam(){
		return "api/team";
	}
	@GetMapping("/outline")
	public String apiOutline(){
		return "api/outline";
	}



	@GetMapping("/auth")
	public String apiAuth(){
		return "api/auth";
	}
}

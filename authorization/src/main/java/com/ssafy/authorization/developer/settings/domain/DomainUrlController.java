package com.ssafy.authorization.developer.settings.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/api/team/domain")
public class DomainUrlController {

	@PostMapping("/regist")
	public void registDomain(@RequestBody JsonNode requestBody) {
		System.out.println(requestBody);
	}
}

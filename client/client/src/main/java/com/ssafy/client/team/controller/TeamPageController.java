package com.ssafy.client.team.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/teams")
public class TeamPageController {

	private final String SUFFIX = "pages/";

	@GetMapping("/{teamSeq}/summary")
	public String summary() {
		return SUFFIX + "summary";
	}

	@GetMapping("/{teamSeq}/dashboard")
	public String dashboard() {

		return SUFFIX + "dashboard";
	}

	@GetMapping("/{teamSeq}/country-ip")
	public String countryIp() {

		return SUFFIX + "country-ip";
	}

	@GetMapping("/{teamSeq}/consents")
	public String consents() {

		return SUFFIX + "consents";
	}

	@GetMapping("/{teamSeq}/management")
	public String management() {

		return SUFFIX + "management";
	}

	@GetMapping("/{teamSeq}/settings")
	public String settings() {

		return SUFFIX + "settings";
	}

}

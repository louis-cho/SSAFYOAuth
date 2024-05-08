package com.ssafy.client.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.client.user.service.ApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamPageController {
	private static final String RESOURCE_SERVER_URL = "http://localhost:8080/api";
	private final ApiService apiService;

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
	public String countryIp(@PathVariable Integer teamSeq, Model model) {
		String url = RESOURCE_SERVER_URL + "/team" + teamSeq + "country-ip";
		ArrayList<?> blockedCountries = apiService.callListApi(url);
		log.debug("블랙 리스트 된 나라들 : {} ", Arrays.toString(blockedCountries.toArray()));
		model.addAttribute("blockedCountries", blockedCountries);
		return "country-ip";
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

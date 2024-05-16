package com.ssafy.client.user.controller;

import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.client.user.dto.ProfileInformationForUpdatesDto;
import com.ssafy.client.user.service.ApiService;
import com.ssafy.client.user.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserPageController {
	private final ApiService apiService;
	private final ObjectMapper objectMapper;
	private final String RESOURCES_URL = "http://localhost:8090";
	private final String Authorization_URL = "http://localhost:9000";


	@GetMapping("/user/sign-up")
	public String signUp(){
		return "user/sign_up";
	}

	@GetMapping("/user/update")
	public String updateUserPage(Model model) {
		try {
			String string = apiService.callProtectedApi(RESOURCES_URL+"/user/data");
			ProfileInformationForUpdatesDto userData = objectMapper.readValue(string,
				ProfileInformationForUpdatesDto.class);
			log.info("{} tttt", userData);
			model.addAttribute("userData", userData);
		} catch (Exception e) {
			log.error("Error while retrieving access token: ", e);
		}
		return "user/update";
	}

	@PostMapping("/user/update")
	public String updateUserPost(@ModelAttribute ProfileInformationForUpdatesDto dto, RedirectAttributes redirectAttributes) {
		try {
			// Assuming the URL is stored in a variable or comes from configuration
			String response = apiService.callPostFileApi(RESOURCES_URL + "/user/info", dto);
			log.info("Update response: {}", response);
			redirectAttributes.addFlashAttribute("success", "성공적으로 저장했습니다!");
		} catch (Exception e) {
			log.error("Error updating profile: ", e);
			redirectAttributes.addFlashAttribute("error", "성공적으로 실패했습니다.");
		}
		return "redirect:/user/update";
	}

	@GetMapping("/user/logout")
	public String logout() {
		try {
			apiService.callProtectedApi(Authorization_URL+"/logout");
		} catch (Exception e) {
			log.error("Error while logout: ", e);
		}
		return "redirect:/login";
	}

}

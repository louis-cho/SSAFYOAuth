package com.ssafy.client.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminPageController {

	@GetMapping("/dashboard")
	public String adminDashboard() {
		return "pages/admin-dashboard";
	}
}

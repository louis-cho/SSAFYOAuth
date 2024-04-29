package com.ssafy.authorization.link.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.authorization.link.service.LinkService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/link")
@RequiredArgsConstructor
@Slf4j
public class LinkController {

	private final LinkService linkService;

	@GetMapping("/list")
	public String linkList(Model model, Authentication authentication){
		Map data = linkService.listLink(authentication);
		model.addAllAttributes(data);
		return "link/service";
	}
}

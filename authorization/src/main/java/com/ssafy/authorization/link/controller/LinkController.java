package com.ssafy.authorization.link.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public String linkList(Model model, Authentication authentication) {
		Map data = linkService.listLink(authentication);
		model.addAllAttributes(data);
		return "subscribe/services";
	}

	@DeleteMapping("/{team-seq}")
	public String linkRemove(@PathVariable("team-seq") Integer teamSeq, Model model, Authentication authentication) {
		Map data = linkService.removeLink(teamSeq, authentication);
		model.addAllAttributes(data);
		return "subscribe/services";
	}

	@GetMapping("/{team-seq}/list")
	public String customerList(@PathVariable("team-seq") Integer teamSeq, Model model, Authentication authentication) {
		Map data = linkService.listCustomer(teamSeq, authentication);
		model.addAllAttributes(data);
		return "subscribe/customers";
	}

	@GetMapping("/{team-seq}/search/{keyword}")
	public String customerSearch(@PathVariable("team-seq") Integer teamSeq, @PathVariable("keyword") String keyword,
		Model model, Authentication authentication) {
		Map data = linkService.searchCustomer(teamSeq, keyword, authentication);
		model.addAllAttributes(data);
		return "subscribe/customers";
	}

	@DeleteMapping("/{team-seq}/{member-seq}")
	public String customerRemove(@PathVariable("team-seq") Integer teamSeq,
		@PathVariable("member-seq") Integer memberSeq, Model model, Authentication authentication) {
		Map data = linkService.removeCustomer(teamSeq, memberSeq, authentication);
		model.addAllAttributes(data);
		return "subscribe/customers";
	}
}

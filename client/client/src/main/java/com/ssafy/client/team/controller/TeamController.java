package com.ssafy.client.team.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/team")
public class TeamController {

	@GetMapping("/detail/{team-seq}")
	public String teamDetail(@PathVariable("team-seq") Integer teamSeq, Model model) {
		model.addAttribute("teamSeq", teamSeq);
		return "team/detail";
	}

}

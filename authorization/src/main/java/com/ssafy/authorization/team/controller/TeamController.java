package com.ssafy.authorization.team.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssafy.authorization.team.service.TeamService;
import com.ssafy.authorization.team.vo.ServiceNameUpdateVo;
import com.ssafy.authorization.team.vo.TeamAddVo;
import com.ssafy.authorization.team.vo.TeamNameUpdateVo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

	private final TeamService teamService;

	@GetMapping
	public String teamList(Model model) {
		Map data = teamService.listTeam();
		model.addAllAttributes(data);
		return "team/list";
	}

	@GetMapping("/add")
	public String getTeamAddPage(){
		return "team/add";
	}

	@PostMapping
	@ResponseBody
	public Map teamAdd(@RequestBody @Valid TeamAddVo vo) {
		Map data = teamService.addTeam(vo);
		return data;
	}

	@GetMapping("/{team-seq}")
	public String teamDetail(@PathVariable("team-seq") Integer teamSeq, Model model) {
		Map data = teamService.detailTeam(teamSeq);
		model.addAllAttributes(data);
		return "team/detail";
	}

	@DeleteMapping("/{team-seq}")
	@ResponseBody
	public Map teamDelete(@PathVariable("team-seq") Integer teamSeq) {
		Map data = teamService.deleteTeam(teamSeq);
		return data;
	}

	@PutMapping("/{team-seq}")
	@ResponseBody
	public Map teamNameUpdate(@PathVariable("team-seq") Integer teamSeq,
		@RequestBody @Valid TeamNameUpdateVo vo) {
		Map data = teamService.updateTeamName(teamSeq, vo);
		return data;
	}

	@PatchMapping("/{team-seq}")
	@ResponseBody
	public Map ServiceNameUpdate(@PathVariable("team-seq") Integer teamSeq,
		@RequestBody @Valid ServiceNameUpdateVo vo) {
		Map data = teamService.updateServiceName(teamSeq, vo);
		return data;
	}

	@GetMapping("/member/{email}")
	@ResponseBody
	public Map developerSearch(@PathVariable("email") String email) {
		return teamService.searchDeveloper(email);
	}

	@PostMapping("/{team-seq}/member/{email}")
	@ResponseBody
	public Map memberAdd(@PathVariable("team-seq") Integer teamSeq,
		@PathVariable("email") String email) {
		return teamService.addMember(teamSeq, email);
	}

	@DeleteMapping("/{team-seq}/member/{email}")
	@ResponseBody
	public Map memberDelete(@PathVariable("team-seq") Integer teamSeq,
		@PathVariable("email") String email) {
		return teamService.deleteMember(teamSeq, email);
	}
}

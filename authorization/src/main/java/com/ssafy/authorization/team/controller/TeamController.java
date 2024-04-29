package com.ssafy.authorization.team.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.authorization.team.service.TeamService;
import com.ssafy.authorization.team.vo.ServiceNameUpdateVo;
import com.ssafy.authorization.team.vo.TeamAddVo;
import com.ssafy.authorization.team.vo.TeamImageVo;
import com.ssafy.authorization.team.vo.TeamNameUpdateVo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

	private final TeamService teamService;

	@GetMapping
	public String teamList(Model model, Authentication authentication) {
		Map data = teamService.listTeam(authentication);
		model.addAllAttributes(data);
		return "team/list";
	}

	@GetMapping("/add")
	public String getTeamAddPage() {
		return "team/add";
	}

	@PostMapping
	@ResponseBody
	public Map teamAdd(@RequestBody @Valid TeamAddVo vo, Authentication authentication) {
		Map data = teamService.addTeam(vo, authentication);
		return data;
	}

	@GetMapping("/{team-seq}")
	@ResponseBody
	public Map teamDetail(@PathVariable("team-seq") Integer teamSeq, Authentication authentication) {
		Map data = teamService.detailTeam(teamSeq, authentication);
		return data;
	}

	@DeleteMapping("/{team-seq}")
	@ResponseBody
	public Map teamDelete(@PathVariable("team-seq") Integer teamSeq, Authentication authentication) {
		Map data = teamService.deleteTeam(teamSeq, authentication);
		return data;
	}

	@PutMapping("/{team-seq}")
	@ResponseBody
	public Map teamNameUpdate(@PathVariable("team-seq") Integer teamSeq,
		@RequestBody @Valid TeamNameUpdateVo vo, Authentication authentication) {
		Map data = teamService.updateTeamName(teamSeq, vo, authentication);
		return data;
	}

	@PatchMapping("/{team-seq}")
	@ResponseBody
	public Map ServiceNameUpdate(@PathVariable("team-seq") Integer teamSeq,
		@RequestBody @Valid ServiceNameUpdateVo vo, Authentication authentication) {
		Map data = teamService.updateServiceName(teamSeq, vo, authentication);
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
		@PathVariable("email") String email, Authentication authentication) {
		return teamService.addMember(teamSeq, email, authentication);
	}

	@DeleteMapping("/{team-seq}/member/{email}")
	@ResponseBody
	public Map memberDelete(@PathVariable("team-seq") Integer teamSeq,
		@PathVariable("email") String email, Authentication authentication) {
		return teamService.deleteMember(teamSeq, email, authentication);
	}

	@PostMapping("/image")
	@ResponseBody
	public Map teamImageUpload(@RequestParam("image") MultipartFile file) {
		return teamService.uploadTeamImage(file);
	}

	@DeleteMapping("/{team-seq}/image")
	@ResponseBody
	public Map teamImageDelete(@PathVariable("team-seq") Integer teamSeq, Authentication authentication) {
		Map data = teamService.deleteTeamImage(teamSeq, authentication);
		return data;
	}

	@PostMapping("/{team-seq}/image")
	@ResponseBody
	public Map teamImageModify(@RequestParam("team-seq") Integer teamSeq, @RequestBody @Valid TeamImageVo vo,
		Authentication authentication) {
		Map data = teamService.modifyTeamImage(teamSeq, vo, authentication);
		return data;
	}

	@GetMapping("/invite")
	public String invitedTeamList(Model model, Authentication authentication){
		Map data = teamService.listInvitedTeam(authentication);
		model.addAllAttributes(data);
		return "team/invite";
	}
}

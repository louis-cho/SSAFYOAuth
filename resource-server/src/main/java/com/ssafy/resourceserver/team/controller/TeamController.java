package com.ssafy.resourceserver.team.controller;

import java.util.Map;

import com.ssafy.resourceserver.team.vo.ServiceNameUpdateVo;
import com.ssafy.resourceserver.team.vo.TeamAddVo;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.resourceserver.team.service.TeamService;
import com.ssafy.resourceserver.team.vo.TeamImageVo;
import com.ssafy.resourceserver.team.vo.TeamNameUpdateVo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {

	private final TeamService teamService;

	@GetMapping
	public Map teamList(Authentication authentication) {
		Map data = teamService.listTeam(authentication);
		return data;
	}

	@PostMapping
	public Map teamAdd(@RequestBody TeamAddVo vo, Authentication authentication) {
		log.info("팀 추가에서 넘어온 값 : {} ", vo);
		Map data = teamService.addTeam(vo, authentication);
		return data;
	}

	@GetMapping("/{team-seq}")
	public Map teamDetail(@PathVariable("team-seq") Integer teamSeq, Authentication authentication) {
		Map data = teamService.detailTeam(teamSeq, authentication);
		return data;
	}

	@DeleteMapping("/{team-seq}")
	public Map teamDelete(@PathVariable("team-seq") Integer teamSeq, Authentication authentication) {
		Map data = teamService.deleteTeam(teamSeq, authentication);
		return data;
	}

	@PutMapping("/{team-seq}")
	public Map teamNameUpdate(@PathVariable("team-seq") Integer teamSeq,
		@RequestBody @Valid TeamNameUpdateVo vo, Authentication authentication) {
		Map data = teamService.updateTeamName(teamSeq, vo, authentication);
		return data;
	}

	@PatchMapping("/{team-seq}")
	public Map ServiceNameUpdate(@PathVariable("team-seq") Integer teamSeq,
                                 @RequestBody @Valid ServiceNameUpdateVo vo, Authentication authentication) {
		Map data = teamService.updateServiceName(teamSeq, vo, authentication);
		return data;
	}

	@GetMapping("/member/{email}")
	public Map developerSearch(@PathVariable("email") String email) {
		return teamService.searchDeveloper(email);
	}

	@PostMapping("/{team-seq}/member/{email}")
	public Map memberAdd(@PathVariable("team-seq") Integer teamSeq,
		@PathVariable("email") String email, Authentication authentication) {
		return teamService.addMember(teamSeq, email, authentication);
	}

	@DeleteMapping("/{team-seq}/member/{email}")
	public Map memberDelete(@PathVariable("team-seq") Integer teamSeq,
		@PathVariable("email") String email, Authentication authentication) {
		return teamService.deleteMember(teamSeq, email, authentication);
	}

	@PostMapping("/image")
	public Map teamImageUpload(@RequestParam("image") MultipartFile file) {
		return teamService.uploadTeamImage(file);
	}

	@DeleteMapping("/{team-seq}/image")
	public Map teamImageDelete(@PathVariable("team-seq") Integer teamSeq, Authentication authentication) {
		Map data = teamService.deleteTeamImage(teamSeq, authentication);
		return data;
	}

	@PostMapping("/{team-seq}/image")
	public Map teamImageModify(@RequestParam("team-seq") Integer teamSeq, @RequestBody @Valid TeamImageVo vo,
		Authentication authentication) {
		Map data = teamService.modifyTeamImage(teamSeq, vo, authentication);
		return data;
	}

	@GetMapping("/invite")
	public Map invitedTeamList(Authentication authentication){
		Map data = teamService.listInvitedTeam(authentication);
		return data;
	}

	@PatchMapping("/invite/{team-seq}")
	public Map acceptInvitation(@PathVariable("team-seq") Integer teamSeq, Model model, Authentication authentication){
		Map data = teamService.acceptInvite(teamSeq, authentication);
		return data;
	}

	@DeleteMapping("/invite/{team-seq}")
	public Map rejectInvitation(@PathVariable("team-seq") Integer teamSeq, Model model, Authentication authentication){
		Map data = teamService.rejectInvite(teamSeq, authentication);
		return data;
	}
}

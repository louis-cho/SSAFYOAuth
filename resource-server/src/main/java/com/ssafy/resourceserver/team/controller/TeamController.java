package com.ssafy.resourceserver.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ssafy.resourceserver.team.vo.ServiceNameUpdateVo;
import com.ssafy.resourceserver.team.vo.TeamAddVo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

import com.ssafy.resourceserver.team.service.TeamService;
import com.ssafy.resourceserver.team.vo.TeamImageVo;
import com.ssafy.resourceserver.team.vo.TeamNameUpdateVo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {

	private final TeamService teamService;

	@GetMapping
	@ResponseBody
	public Map teamList(@AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.listTeam(email);
		log.info("data는 이거다 {}", data);
		return data;
	}

	@PostMapping
	@ResponseBody
	public Map teamAdd(@RequestBody TeamAddVo vo, @AuthenticationPrincipal Jwt jwt) {
		log.info("팀 추가에서 넘어온 값 : {} ", vo);
		String email = jwt.getClaimAsString("sub");
		log.info("이메일은 {}", email);
		Map data = teamService.addTeam(vo, email);
		return data;
	}
	@GetMapping("/test")
	@ResponseBody
	public Map teamAdd() {
		log.info("Testsetstestestse");
		Map data = new HashMap();
		data.put("123", "#@!");
		return data;
	}
	@GetMapping("/{team-seq}")
	@ResponseBody
	public Map teamDetail(@PathVariable("team-seq") Integer teamSeq, @AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.detailTeam(teamSeq, email);
		return data;
	}

	@PostMapping("/delete/{team-seq}")
	@ResponseBody
	public Map teamDelete(@PathVariable("team-seq") Integer teamSeq, @AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.deleteTeam(teamSeq, email);
		return data;
	}

	@PostMapping("/teamname/{team-seq}")
	@ResponseBody
	public Map teamNameUpdate(@PathVariable("team-seq") Integer teamSeq,
		@RequestBody @Valid TeamNameUpdateVo vo, @AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.updateTeamName(teamSeq, vo, email);
		return data;
	}

	@PostMapping("/servicename/{team-seq}")
	@ResponseBody
	public Map ServiceNameUpdate(@PathVariable("team-seq") Integer teamSeq,
		@RequestBody @Valid ServiceNameUpdateVo vo, @AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.updateServiceName(teamSeq, vo, email);
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
		@PathVariable("email") String email, @AuthenticationPrincipal Jwt jwt) {
		String myEmail = jwt.getClaimAsString("sub");
		return teamService.addMember(teamSeq, email, myEmail);
	}

	@DeleteMapping("/{team-seq}/member/{email}/delete")
	@ResponseBody
	public Map memberDelete(@PathVariable("team-seq") Integer teamSeq,
		@PathVariable("email") String email, @AuthenticationPrincipal Jwt jwt) {
		String myEmail = jwt.getClaimAsString("sub");
		return teamService.deleteMember(teamSeq, email, myEmail);
	}

	@PostMapping("/image")
	@ResponseBody
	public Map teamImageUpload(@RequestParam("image") MultipartFile file) {
		return teamService.uploadTeamImage(file);
	}

	@PostMapping("/{team-seq}/image/delete")
	@ResponseBody
	public Map teamImageDelete(@PathVariable("team-seq") Integer teamSeq, @AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.deleteTeamImage(teamSeq, email);
		return data;
	}

	@PostMapping("/{team-seq}/image")
	@ResponseBody
	public Map teamImageModify(@PathVariable("team-seq") Integer teamSeq, @RequestBody @Valid TeamImageVo vo, @AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.modifyTeamImage(teamSeq, vo, email);
		return data;
	}

	@GetMapping("/invite")
	public String invitedTeamList(Model model, @AuthenticationPrincipal Jwt jwt){
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.listInvitedTeam(email);
		model.addAllAttributes(data);
		return "team/invite";
	}

	@PatchMapping("/invite/{team-seq}")
	public String acceptInvitation(@PathVariable("team-seq") Integer teamSeq, Model model, @AuthenticationPrincipal Jwt jwt){
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.acceptInvite(teamSeq, email);
		model.addAllAttributes(data);
		return "team/invite";
	}

	@PostMapping("/invite/{team-seq}/reject")
	public String rejectInvitation(@PathVariable("team-seq") Integer teamSeq, Model model, @AuthenticationPrincipal Jwt jwt){
		String email = jwt.getClaimAsString("sub");
		Map data = teamService.rejectInvite(teamSeq, email);
		model.addAllAttributes(data);
		return "team/invite";
	}

	@GetMapping("/{teamSeq}/country-ip")
	@ResponseBody
	public ResponseEntity<?> getAllBlockedCountries(@PathVariable Integer teamSeq ,@AuthenticationPrincipal Jwt jwt) {
		List<String> arr = teamService.getBlockedCountriesByTeamId(teamSeq);

		return ResponseEntity.status(HttpStatus.OK).body(arr);
	}

	@PostMapping("/{teamSeq}/country-ip")
	@ResponseBody
	public ResponseEntity<?> createBlockedCountries(@RequestBody List<String> countries, @PathVariable Integer teamSeq ,@AuthenticationPrincipal Jwt jwt) {
		log.info("post country {}", countries);
		boolean result = teamService.updateBlockedCountries(countries);
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
}

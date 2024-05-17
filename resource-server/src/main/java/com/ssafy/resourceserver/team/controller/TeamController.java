package com.ssafy.resourceserver.team.controller;

import com.ssafy.resourceserver.team.service.TeamService;
import com.ssafy.resourceserver.team.vo.ServiceNameUpdateVo;
import com.ssafy.resourceserver.team.vo.TeamAddVo;
import com.ssafy.resourceserver.team.vo.TeamImageVo;
import com.ssafy.resourceserver.team.vo.TeamNameUpdateVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@PostMapping("/{team-seq}/member/{email}/delete")
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
		boolean result = teamService.updateBlockedCountries(countries,teamSeq);
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}

	@GetMapping("/{teamSeq}/count-user")
	public ResponseEntity<?> countServiceUser(@PathVariable Integer teamSeq) {
		log.info("팀 아이디 : {}", teamSeq);
		Map data = teamService.countServiceUser(teamSeq);
		return ResponseEntity.status(HttpStatus.OK).body(data);
	}

	@GetMapping("/{teamSeq}/count-login-user")
	public ResponseEntity<?> countLoginUser(@PathVariable Integer teamSeq) {
		log.info("팀 아이디 : {}", teamSeq);
		Map data = teamService.countLoginUser(teamSeq);
		return ResponseEntity.status(HttpStatus.OK).body(data);
	}

	@GetMapping("/{teamSeq}/abnormal-login")
	public ResponseEntity<?> abnormalLogin(@PathVariable Integer teamSeq) {
		log.info("팀 아이디 22: {}", teamSeq);
		Map data = teamService.abnormalLogin(teamSeq);
		return ResponseEntity.status(HttpStatus.OK).body(data);
	}

	@GetMapping("/count-all-login-user")
	public ResponseEntity<?> countAllLoginUser() {
		int loginCount = teamService.countAllLoginUser();
		return ResponseEntity.status(HttpStatus.OK).body(loginCount);
	}

	@GetMapping("/all-abnormal-login")
	public ResponseEntity<?> allAbnormalLogin() {
		Long allAbnormalCount = teamService.allAbnormalLogin();
		return ResponseEntity.status(HttpStatus.OK).body(allAbnormalCount);
	}
}

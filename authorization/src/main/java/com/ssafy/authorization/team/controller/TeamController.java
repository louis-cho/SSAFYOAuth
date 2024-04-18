package com.ssafy.authorization.team.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.authorization.team.service.TeamService;
import com.ssafy.authorization.team.vo.TeamAddVo;
import com.ssafy.authorization.team.vo.TeamNameUpdateVo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

	private final TeamService teamService;

	@GetMapping
	public ResponseEntity<Map> teamList(){
		return new ResponseEntity<Map>(teamService.listTeam(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Map> teamAdd(@RequestBody @Valid TeamAddVo vo){
		return new ResponseEntity<Map>(teamService.addTeam(vo), HttpStatus.OK);
	}

	@DeleteMapping("/{team-seq}")
	public ResponseEntity<Map> teamDelete(@PathVariable("team-seq") Integer teamSeq){
		return new ResponseEntity<Map>(teamService.deleteTeam(teamSeq), HttpStatus.OK);
	}

	@PutMapping("/{team-seq}")
	public ResponseEntity<Map> teamNameUpdate(@PathVariable("team-seq") Integer teamSeq, @RequestBody @Valid TeamNameUpdateVo vo){
		return new ResponseEntity<Map>(teamService.updateTeamName(teamSeq, vo), HttpStatus.OK);
	}
}

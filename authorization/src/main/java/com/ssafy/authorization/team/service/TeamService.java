package com.ssafy.authorization.team.service;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.authorization.team.vo.ServiceNameUpdateVo;
import com.ssafy.authorization.team.vo.TeamAddVo;
import com.ssafy.authorization.team.vo.TeamImageVo;
import com.ssafy.authorization.team.vo.TeamNameUpdateVo;

public interface TeamService {
	Map<String, Object> addTeam(TeamAddVo vo, Authentication authentication);

	Map deleteTeam(Integer teamSeq, Authentication authentication);

	Map updateTeamName(Integer teamSeq, TeamNameUpdateVo vo, Authentication authentication);

	Map listTeam(Authentication authentication);

	Map detailTeam(Integer teamSeq, Authentication authentication);

	Map updateServiceName(Integer teamSeq, ServiceNameUpdateVo vo, Authentication authentication);

	Map searchDeveloper(String email);

	Map addMember(Integer teamSeq, String email, Authentication authentication);

	Map deleteMember(Integer teamSeq, String email, Authentication authentication);

	Map uploadTeamImage(MultipartFile file);

	Map deleteTeamImage(Integer teamSeq, Authentication authentication);

	Map modifyTeamImage(Integer teamSeq, TeamImageVo vo, Authentication authentication);
}

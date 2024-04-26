package com.ssafy.authorization.team.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.authorization.team.vo.ServiceNameUpdateVo;
import com.ssafy.authorization.team.vo.TeamAddVo;
import com.ssafy.authorization.team.vo.TeamImageVo;
import com.ssafy.authorization.team.vo.TeamNameUpdateVo;

public interface TeamService {
	Map<String, Object> addTeam(TeamAddVo vo);

	Map deleteTeam(Integer teamSeq);

	Map updateTeamName(Integer teamSeq, TeamNameUpdateVo vo);

	Map listTeam();

	Map detailTeam(Integer teamSeq);

	Map updateServiceName(Integer teamSeq, ServiceNameUpdateVo vo);

	Map searchDeveloper(String email);

	Map addMember(Integer teamSeq, String email);

	Map deleteMember(Integer teamSeq, String email);

	Map uploadTeamImage(MultipartFile file);

	Map deleteTeamImage(Integer teamSeq);

	Map modifyTeamImage(Integer teamSeq, TeamImageVo vo);
}

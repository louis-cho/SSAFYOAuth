package com.ssafy.authorization.team.service;

import java.util.Map;

import com.ssafy.authorization.team.vo.TeamAddVo;
import com.ssafy.authorization.team.vo.TeamNameUpdateVo;

public interface TeamService {
	Map<String, Object> addTeam(TeamAddVo vo);

	Map deleteTeam(Integer teamSeq);

	Map updateTeamName(Integer teamSeq, TeamNameUpdateVo vo);
}

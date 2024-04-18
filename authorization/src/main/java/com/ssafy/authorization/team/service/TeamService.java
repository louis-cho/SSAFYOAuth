package com.ssafy.authorization.team.service;

import java.util.Map;

import com.ssafy.authorization.team.vo.TeamAddVo;

public interface TeamService {
	Map<String, Object> addTeam(TeamAddVo vo);
}

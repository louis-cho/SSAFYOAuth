package com.ssafy.resourceserver.team.service;

import java.util.List;
import java.util.Map;

import com.ssafy.resourceserver.team.vo.ServiceNameUpdateVo;
import com.ssafy.resourceserver.team.vo.TeamAddVo;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.resourceserver.team.vo.TeamImageVo;
import com.ssafy.resourceserver.team.vo.TeamNameUpdateVo;

public interface TeamService {
	Map<String, Object> addTeam(TeamAddVo vo, String email);

	Map deleteTeam(Integer teamSeq, String email);

	Map updateTeamName(Integer teamSeq, TeamNameUpdateVo vo, String email);

	Map listTeam(String email);

	Map detailTeam(Integer teamSeq, String email);

	Map updateServiceName(Integer teamSeq, ServiceNameUpdateVo vo, String email);

	Map searchDeveloper(String email);

	Map addMember(Integer teamSeq, String email, String myEmail);

	Map deleteMember(Integer teamSeq, String email, String myEmail);

	Map uploadTeamImage(MultipartFile file);

	Map deleteTeamImage(Integer teamSeq, String email);

	Map modifyTeamImage(Integer teamSeq, TeamImageVo vo, String email);

	Map listInvitedTeam(String email);

	Map acceptInvite(Integer teamSeq, String email);

	Map rejectInvite(Integer teamSeq, String email);

	List<String> getBlockedCountriesByTeamId(Integer teamSeq);

	boolean updateBlockedCountries(List<String> countries);
}

package com.ssafy.authorization.team.service;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.parser.Entity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.authorization.team.dto.TeamAddDto;
import com.ssafy.authorization.team.entity.DeveloperTeamEntity;
import com.ssafy.authorization.team.entity.TeamMemberEntity;
import com.ssafy.authorization.team.repository.DeveloperTeamRepository;
import com.ssafy.authorization.team.repository.TeamMemberRepository;
import com.ssafy.authorization.team.vo.TeamAddVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

	private final DeveloperTeamRepository developerTeamRepository;

	private final TeamMemberRepository teamMemberRepository;

	@Override
	@Transactional
	public Map<String, Object> addTeam(TeamAddVo vo) {
		Map<String, Object> data = new HashMap<>();
		if(vo.getTeamMember().length > 5){
			data.put("msg", "팀원은 자신 포함 6명을 넘길 수 없습니다.");
			data.put("team_seq", null);
			return data;
		}
		if(vo.getDomainUrl().length > 5){
			data.put("msg", "도메인은 5개까지 등록할 수 있습니다.");
			data.put("team_seq", null);
			return data;
		}
		if(vo.getRedirectionUrl().length > 10){
			data.put("msg", "리다이렉트 url은 10개까지 등록할 수 있습니다.");
			data.put("team_seq", null);
			return data;
		}
		// 팀원들이 모두 등록된 개발자 인지 확인
		for(String member : vo.getTeamMember()){
			boolean is_exist = true;
			// 존재 하지 않는 팀 원이면
			if(!is_exist){
				data.put("msg", "팀원 목록에 존재 하지 않는 개발자 이메일이 있습니다.");
				data.put("team_seq", null);
				return data;
			}
		}
		TeamAddDto dto = new TeamAddDto();
		dto.setTeamName(vo.getTeamName());
		dto.setServiceName(vo.getServiceName());
		String[] teamMembers = new String[vo.getTeamMember().length + 1];
		for(int i = 0; i < vo.getTeamMember().length; i++){
			teamMembers[i] = vo.getTeamMember()[i];
		}
		// 자기 자신의 email을 추가
		String myEmail = "자기 자신의 이메일 검색 해서 대입";
		teamMembers[vo.getTeamMember().length] = myEmail;
		dto.setDomainUrl(vo.getDomainUrl());
		dto.setRedirectUrl(vo.getRedirectionUrl());
		// 자기 자신의 seq를 team leader seq로 지정
		int mySeq = 0;
		dto.setLeaderMemberSeq(mySeq);

		// 팀 생성
		DeveloperTeamEntity entity = new DeveloperTeamEntity();
		entity.setServiceName(dto.getServiceName());
		entity.setTeamName(dto.getTeamName());
		entity.setServiceName(dto.getServiceName());
		Integer teamSeq = developerTeamRepository.save(entity).getSeq();

		// 팀원 생성
		for(String email : dto.getMembers()){
			// email로 member_seq읽기
			Integer seq = 0;
			//멤버로 추가
			teamMemberRepository.save(new TeamMemberEntity(teamSeq, seq, seq == mySeq ? true : false));
		}
		// 도메인 등록

		// 리다이렉트 url 등록


		return data;
	}
}

package com.ssafy.authorization.team.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.text.html.parser.Entity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.authorization.team.dto.TeamAddDto;
import com.ssafy.authorization.team.entity.DeveloperTeamEntity;
import com.ssafy.authorization.team.entity.TeamListEntity;
import com.ssafy.authorization.team.entity.TeamMemberEntity;
import com.ssafy.authorization.team.entity.TeamMemberPK;
import com.ssafy.authorization.team.repository.DeveloperTeamRepository;
import com.ssafy.authorization.team.repository.TeamListRepository;
import com.ssafy.authorization.team.repository.TeamMemberRepository;
import com.ssafy.authorization.team.vo.TeamAddVo;
import com.ssafy.authorization.team.vo.TeamListVo;
import com.ssafy.authorization.team.vo.TeamNameUpdateVo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

	private final DeveloperTeamRepository developerTeamRepository;

	private final TeamMemberRepository teamMemberRepository;

	private final TeamListRepository teamListRepository;

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

		data.put("msg", null);
		data.put("team_seq", teamSeq);
		return data;
	}

	@Override
	public Map deleteTeam(Integer teamSeq) {
		Map<String, String> data = new HashMap<>();
		// 요청된 팀이 존재하는지 확인
		List<DeveloperTeamEntity> list = developerTeamRepository.findBySeqAndIsDeleteFalse(teamSeq);
		if(list.size() != 1){
			data.put("msg", "존재 하지 않는 팀");
			return data;
		}
		// 요청한 사람의 시퀀스 넘버 확인
		Integer mySeq = 0;
		// 요청한 사람이 팀의 리더 인지 파악
		DeveloperTeamEntity entity = list.get(0);
		if(entity.getLeader() != mySeq){
			data.put("msg", "삭제 권한이 없습니다.");
			return data;
		}
		entity.setIsDeleted(true);
		entity.setDeleteDate(LocalDateTime.now());
		developerTeamRepository.save(entity);
		data.put("msg", "삭제되었습니다.");
;		return data;
	}

	@Override
	public Map updateTeamName(Integer teamSeq, TeamNameUpdateVo vo) {
		Map<String, String> data = new HashMap<>();

		// 요청된 팀이 존재 하는지 확인
		List<DeveloperTeamEntity> list = developerTeamRepository.findBySeqAndIsDeleteFalse(teamSeq);
		if(list.size() != 1){
			data.put("msg", "존재하지 않는 팀");
			data.put("team_name", null);
			return data;
		}

		// 자신의 시퀀스 넘버 확인
		Integer mySeq = 0;

		// 자신이 요청한 팀의 팀원인지 확인
		Optional<TeamMemberEntity> member = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if(member.isEmpty()){
			data.put("msg", "팀명을 수정할 수 있는 권한이 없습니다.");
			data.put("team_name", null);
			return data;
		}

		// 팀 명 수정
		DeveloperTeamEntity entity = list.get(0);
		entity.setTeamName(vo.getTeamName());
		entity = developerTeamRepository.save(entity);
		data.put("msg", null);
		data.put("team_name", entity.getTeamName());
		return data;
	}

	@Override
	public Map listTeam() {
		Map<String, Object> data = new HashMap<>();
		// 자신의 시퀀스 넘버를 확인
		Integer mySeq = 0;
		List<TeamListEntity> entities = teamListRepository.findByMemberSeq(mySeq);
		if(entities.isEmpty()){
			data.put("msg", "소속된 팀이 존재하지 않습니다.");
			data.put("list", null);
			return data;
		}
		List<TeamListVo> vos = entities.stream().map(entity ->{
			TeamListVo vo = new TeamListVo();
			vo.setTeamName(entity.getTeamName());
			vo.setServiceName(entity.getServiceName());
			vo.setTeamSeq(entity.getTeamSeq());
			vo.setIsLeader(mySeq == entity.getLeader() ? true : false);
			vo.setIsAccept(entity.getIsAccept());
			vo.setCreateDate(entity.getCreateDate());
			vo.setModifyDate(entity.getModifyDate());
			return vo;
		}).collect(Collectors.toList());
		data.put("msg", null);
		data.put("list", vos);
		return data;
	}
}

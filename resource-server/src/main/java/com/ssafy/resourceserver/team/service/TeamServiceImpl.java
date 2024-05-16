package com.ssafy.resourceserver.team.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ssafy.resourceserver.redirect.repository.RedirectEntityRepository;
import com.ssafy.resourceserver.team.entity.BlockedCountriesEntity;
import com.ssafy.resourceserver.team.repository.BlockedCountriesRepository;
import com.ssafy.resourceserver.team.entity.CountLoginUserEntity;
import com.ssafy.resourceserver.team.repository.CountLoginUserRepository;
import com.ssafy.resourceserver.team.repository.DeveloperTeamRepository;
import com.ssafy.resourceserver.team.repository.TeamListRepository;
import com.ssafy.resourceserver.team.repository.TeamMemberRepository;
import com.ssafy.resourceserver.team.repository.TeamMemberWithInfoRepository;
import com.ssafy.resourceserver.team.vo.DeveloperSearchVo;
import com.ssafy.resourceserver.team.vo.InviteListVo;
import com.ssafy.resourceserver.team.vo.ServiceNameUpdateVo;
import com.ssafy.resourceserver.team.vo.TeamAddVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.resourceserver.member.model.repository.MemberRepository;
import com.ssafy.resourceserver.redirect.model.RedirectEntity;
import com.ssafy.resourceserver.redirect.service.RedirectService;
import com.ssafy.resourceserver.team.dto.TeamAddDto;
import com.ssafy.resourceserver.team.entity.DeveloperMemberEntity;
import com.ssafy.resourceserver.team.entity.DeveloperTeamEntity;
import com.ssafy.resourceserver.team.entity.TeamListEntity;
import com.ssafy.resourceserver.team.entity.TeamMemberEntity;
import com.ssafy.resourceserver.team.entity.TeamMemberPK;
import com.ssafy.resourceserver.team.entity.TeamMemberWithInfoEntity;
import com.ssafy.resourceserver.team.repository.DeveloperMemberRepository;
import com.ssafy.resourceserver.team.vo.TeamDetailVo;
import com.ssafy.resourceserver.team.vo.TeamImageVo;
import com.ssafy.resourceserver.team.vo.TeamListVo;
import com.ssafy.resourceserver.team.vo.TeamMemberVo;
import com.ssafy.resourceserver.team.vo.TeamNameUpdateVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
	private final DeveloperTeamRepository developerTeamRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final TeamListRepository teamListRepository;
	private final TeamMemberWithInfoRepository teamMemberWithInfoRepository;
	private final DeveloperMemberRepository developerMemberRepository;
	private final AmazonS3Client s3client;
	private final MemberRepository memberRepository;
	private final RedirectService redirectService;
	private final RedirectEntityRepository redirectRepository;
	private final Oauth2AuthorizationConsentRepository oauth2AuthorizationConsentRepository;
	private final CountLoginUserRepository countLoginUserRepository;

	private boolean test = true;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Override
	@Transactional
	public Map<String, Object> addTeam(TeamAddVo vo, String myEmail) {
		Map<String, Object> data = new HashMap<>();
		if (vo.getTeamMember() != null && vo.getTeamMember().length > 5) {
			data.put("msg", "팀원은 자신 포함 6명을 넘길 수 없습니다.");
			data.put("team_seq", null);
			return data;
		}
		if (vo.getDomainUrl() != null && vo.getDomainUrl().length > 5) {
			data.put("msg", "도메인은 5개까지 등록할 수 있습니다.");
			data.put("team_seq", null);
			return data;
		}
		if (vo.getRedirectionUrl() != null && vo.getRedirectionUrl().length > 10) {
			data.put("msg", "리다이렉트 url은 10개까지 등록할 수 있습니다.");
			data.put("team_seq", null);
			return data;
		}
		// 팀원들이 모두 등록된 개발자 인지 확인

		if (vo.getTeamMember() != null)
			for (String member : vo.getTeamMember()) {
				boolean is_exist = true;
				// 존재 하지 않는 팀 원이면
				List<DeveloperMemberEntity> l = developerMemberRepository.findAllByEmail(member);
				if (l.isEmpty()) {
					is_exist = false;
				}
				if (!is_exist) {
					data.put("msg", "팀원 목록에 존재 하지 않는 개발자 이메일이 있습니다.");
					data.put("team_seq", null);
					return data;
				}
			}
		TeamAddDto dto = new TeamAddDto();
		dto.setTeamName(vo.getTeamName());
		dto.setServiceName(vo.getServiceName());
		String[] teamMembers = new String[vo.getTeamMember() == null ? 1 : vo.getTeamMember().length + 1];
		for (int i = 0; i < (vo.getTeamMember() == null ? 0 : vo.getTeamMember().length); i++) {
			teamMembers[i] = vo.getTeamMember()[i];
		}
		// 자기 자신의 email을 추가
		Integer mySeq =memberRepository.findByEmail(myEmail).get().getMemberId();
		teamMembers[vo.getTeamMember() == null ? 0 : vo.getTeamMember().length] = myEmail;
		dto.setDomainUrl(vo.getDomainUrl());
		dto.setRedirectUrl(vo.getRedirectionUrl());
		// 자기 자신의 seq를 team leader seq로 지정
		dto.setLeaderMemberSeq(mySeq);

		// 팀 생성
		DeveloperTeamEntity entity = DeveloperTeamEntity.CreateTeam(dto, mySeq,UUID.randomUUID().toString());
		System.out.println(entity);
		Integer teamSeq = developerTeamRepository.save(entity).getSeq();

		// 팀원 저장
		for(String teamMember : teamMembers){
			Integer seq = developerMemberRepository.findAllByEmail(teamMember).get(0).getMemberSeq();
			teamMemberRepository.save(new TeamMemberEntity(entity.getSeq(), seq, seq == mySeq ? true : false));
		}

		// 도메인 url 등록

		// 리다이렉트 url 등록
		for (int i = 0; i < (dto.getRedirectUrl() == null ? 0 : dto.getRedirectUrl().length); i++) {
			RedirectEntity e = new RedirectEntity();
			e.setTeamId(teamSeq);
			e.setRedirect(dto.getRedirectUrl()[i]);
			redirectService.insertRedirect(e);
		}

		data.put("msg", null);
		data.put("team_seq", teamSeq);
		data.put("vo", vo);
		return data;
	}

	@Override
	@Transactional
	public Map deleteTeam(Integer teamSeq, String email) {
		Map<String, String> data = new HashMap<>();
		// 요청된 팀이 존재하는지 확인
		List<DeveloperTeamEntity> list = developerTeamRepository.findBySeqAndIsDeleteFalse(teamSeq);
		if (list.size() != 1) {
			data.put("msg", "존재 하지 않는 팀");
			return data;
		}
		// 요청한 사람의 시퀀스 넘버 확인
		String myEmail = email;
		Integer mySeq =     memberRepository.findByEmail(myEmail).get().getMemberId();
		// 요청한 사람이 팀의 리더 인지 파악
		DeveloperTeamEntity entity = list.get(0);
		if (entity.getLeader() != mySeq) {
			data.put("msg", "삭제 권한이 없습니다.");
			return data;
		}
		entity.setIsDelete(true);
		entity.setDeleteDate(LocalDateTime.now());
		developerTeamRepository.save(entity);
		data.put("msg", "삭제되었습니다.");
		return data;
	}

	@Override
	@Transactional
	public Map updateTeamName(Integer teamSeq, TeamNameUpdateVo vo, String email) {
		Map<String, String> data = new HashMap<>();

		// 요청된 팀이 존재 하는지 확인
		List<DeveloperTeamEntity> list = developerTeamRepository.findBySeqAndIsDeleteFalse(teamSeq);
		if (list.size() != 1) {
			data.put("msg", "존재하지 않는 팀");
			data.put("team_name", null);
			return data;
		}

		// 자신의 시퀀스 넘버 확인
		String myEmail = email;
		Integer mySeq =     memberRepository.findByEmail(myEmail).get().getMemberId();

		// 자신이 요청한 팀의 팀원인지 확인
		Optional<TeamMemberEntity> member = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if (member.isEmpty()) {
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
	@Transactional(readOnly = true)
	public Map listTeam(String email) {
		Map<String, Object> data = new HashMap<>();
		// 자신의 시퀀스 넘버를 확인
		Integer mySeq =     memberRepository.findByEmail(email).get().getMemberId();
		List<TeamListEntity> entities = teamListRepository.findByMemberSeq(mySeq);
		if (entities.isEmpty()) {
			data.put("msg", "소속된 팀이 존재하지 않습니다.");
			data.put("list", null);
			return data;
		}
		List<TeamListVo> vos = entities.stream().map(entity -> {
			TeamListVo vo = new TeamListVo();
			vo.setTeamName(entity.getTeamName());
			vo.setServiceName(entity.getServiceName());
			vo.setTeamSeq(entity.getTeamSeq());
			vo.setIsLeader(mySeq == entity.getLeader() ? true : false);
			vo.setIsAccept(entity.getIsAccept());
			vo.setCreateDate(entity.getCreateDate());
			vo.setModifyDate(entity.getModifyDate());
			vo.setClientId(entity.getClientId());
			vo.setServiceKey(entity.getServiceKey());
			return vo;
		}).collect(Collectors.toList());
		data.put("msg", null);
		data.put("list", vos);
		return data;
	}

	@Override
	@Transactional(readOnly = true)
	public Map detailTeam(Integer teamSeq, String email) {
		Map<String, Object> data = new HashMap<>();
		// 존재 하는 팀인지 확인
		List<DeveloperTeamEntity> list = developerTeamRepository.findBySeqAndIsDeleteFalse(teamSeq);
		if (list.size() != 1) {
			data.put("msg", "존재하지 않는 팀");
			data.put("team_name", null);
			return data;
		}

		// 요청을 한 사람이 팀 원인지 확인
		String myEmail = email;
		Integer mySeq = memberRepository.findByEmail(myEmail).get().getMemberId();
		Optional<TeamMemberEntity> member = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if (member.isEmpty()) {
			data.put("msg", "팀을 볼 수 있는 권한이 없습니다.");
			data.put("team_name", null);
			return data;
		}

		// 정상 응답의 경우
		DeveloperTeamEntity team = list.get(0);
		TeamDetailVo vo = new TeamDetailVo();
		vo.setTeamName(team.getTeamName());
		vo.setServiceName(team.getServiceName());
		vo.setServiceKey(team.getServiceKey());
		vo.setImage(team.getServiceImage());
		vo.setClientId(team.getClientId());
		List<TeamMemberVo> memberList = teamMemberWithInfoRepository.findAllByTeamSeq(teamSeq).stream().map(entity -> {
			TeamMemberVo memberVo = new TeamMemberVo();
			memberVo.setEmail(entity.getEmail());
			memberVo.setName(entity.getName());
			memberVo.setIsAccept(entity.getIsAccept());
			memberVo.setIsLeader(entity.getMemberSeq() == team.getLeader() ? true : false);
			return memberVo;
		}).collect(Collectors.toList());
		vo.setMembers(memberList);
		// 도메인과 리디이렉트 url 셋팅
		List<RedirectEntity> redirectEntities = redirectRepository.findAllByTeamId(teamSeq);
		if(!redirectEntities.isEmpty()){
			List<String> redirectUrls = redirectEntities.stream().map(e -> {
				return e.getRedirect();
			}).collect(Collectors.toList());
			vo.setRedirectUrl(redirectUrls);
		}
		//응답
		data.put("msg", null);
		data.put("team", vo);
		return data;
	}

	@Override
	@Transactional
	public Map updateServiceName(Integer teamSeq, ServiceNameUpdateVo vo, String email) {
		Map<String, String> data = new HashMap<>();
		// 팀이 존재 하는지 확인
		List<DeveloperTeamEntity> list = developerTeamRepository.findBySeqAndIsDeleteFalse(teamSeq);
		if (list.size() != 1) {
			data.put("msg", "존재하지 않는 팀");
			data.put("team_name", null);
			return data;
		}
		// 사용자가 팀의 멤버인지 확인
		String myEmail = email;
		Integer mySeq =     memberRepository.findByEmail(myEmail).get().getMemberId();
		Optional<TeamMemberEntity> member = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if (member.isEmpty()) {
			data.put("msg", "팀을 볼 수 있는 권한이 없습니다.");
			data.put("team_name", null);
			return data;
		}
		// 팀의 이름 변경
		DeveloperTeamEntity team = list.get(0);
		team.setServiceName(vo.getServiceName());
		team = developerTeamRepository.save(team);
		// 정상 응답
		data.put("serviceName", team.getServiceName());
		data.put("msg", null);
		return data;
	}

	@Override
	@Transactional(readOnly = true)
	public Map searchDeveloper(String email) {
		Map<String, Object> data = new HashMap<>();
		List<DeveloperMemberEntity> list = developerMemberRepository.findAllByEmailContains(email);
		if (list.size() == 0) {
			data.put("list", null);
			data.put("msg", "검색 결과 없음");
			return data;
		}
		List<DeveloperSearchVo> vos = list.stream().map(entity -> {
			return new DeveloperSearchVo(entity.getEmail(), entity.getName(), entity.getIamge());
		}).collect(Collectors.toList());
		data.put("msg", null);
		data.put("list", vos);
		return data;
	}

	@Override
	@Transactional
	public Map addMember(Integer teamSeq, String email, String myEmail) {
		Map<String, Object> data = new HashMap<>();
		// 팀에 멤버를 추가할 권한이 있는지 확인

		Integer mySeq = memberRepository.findByEmail(myEmail).get().getMemberId();
		Optional<TeamMemberEntity> isMember = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if (isMember.isEmpty()) {
			data.put("msg", "팀에 멤버를 추가할 권한이 없습니다");
			data.put("member", null);
			return data;
		}

		// 팀에 이미 추가된 멤버인지 확인
		List<TeamMemberWithInfoEntity> isTeamMember = teamMemberWithInfoRepository.findAllByTeamSeqAndEmail(teamSeq,
			email);
		if (!isTeamMember.isEmpty()) {
			data.put("msg", "이미 팀에 멤버로 추가된 개발자 입니다.");
			data.put("member", null);
			return data;
		}

		// 팀에 멤버를 추가 할 수 있는 자리가 있는지 확인
		Integer cnt = teamMemberRepository.countByTeamSeq(teamSeq);
		if (cnt >= 6) {
			data.put("msg", "한 팀에 멤버는 최대 6명 입니다. 멤버를 추가 하려면 기존 멤버를 지워 주세요");
			data.put("member", null);
			return data;
		}

		// 해당 개발자의 시퀀스 넘버 확인
		List<DeveloperMemberEntity> dm = developerMemberRepository.findAllByEmail(email);
		if (dm.isEmpty()) {
			data.put("msg", "개발자로 등록된 이메일이 아닙니다.");
			data.put("member", null);
			return data;
		}
		Integer memberSeq = dm.get(0).getMemberSeq();

		// 팀에 멤버 추가
		TeamMemberEntity teamMemberEntity = teamMemberRepository.save(new TeamMemberEntity(teamSeq, memberSeq, false));
		DeveloperMemberEntity e = dm.get(0);
		data.put("msg", null);
		data.put("member", new DeveloperSearchVo(e.getEmail(), e.getName(), e.getIamge()));
		return data;
	}

	@Override
	@Transactional
	public Map deleteMember(Integer teamSeq, String email, String myEmail) {
		Map<String, Object> data = new HashMap<>();
		// 팀에 멤버를 삭제할 권한이 있는지 확인

		Integer mySeq =     memberRepository.findByEmail(myEmail).get().getMemberId();
		Optional<TeamMemberEntity> isMember = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if (isMember.isEmpty()) {
			data.put("msg", "팀 멤버를 수정할 권한이 없습니다");
			return data;
		}

		// 해당 이메일의 멤버가
		// 팀에 포함된 멤버인지 확인
		List<TeamMemberWithInfoEntity> list = teamMemberWithInfoRepository.findAllByTeamSeqAndEmail(teamSeq, email);
		if (list.isEmpty()) {
			data.put("msg", "해당 이메일의 개발자는  팀 멤버가 아닙니다.");
			return data;
		}
		// 팀의 리더인지 확인 -> 리더는 삭제 될 수 없음
		Optional<DeveloperTeamEntity> teamOptional = developerTeamRepository.findById(teamSeq);
		if (teamOptional.isEmpty()) {
			data.put("msg", "팀이 존재하지 않습니다.");
			return data;
		}
		DeveloperTeamEntity team = teamOptional.get();
		Integer leaderSeq = team.getLeader();
		Optional<DeveloperMemberEntity> dmOptional = developerMemberRepository.findById(leaderSeq);
		if (!dmOptional.isEmpty()) {
			DeveloperMemberEntity dm = dmOptional.get();
			if (dm.getEmail().equals(email)) {
				data.put("msg", "팀 리더는 삭제할 수 없습니다.");
				return data;
			}
		}
		// 삭제할 멤버의 시퀀스 넘버 확인
		Integer memberSeq = list.get(0).getMemberSeq();
		// 팀에서 삭제
		teamMemberRepository.deleteById(new TeamMemberPK(teamSeq, memberSeq));
		data.put("msg", null);
		return data;
	}

	@Override
	@Transactional
	public Map uploadTeamImage(MultipartFile file) {
		Map<String, String> data = new HashMap<>();
		String filename = file.getOriginalFilename() + UUID.randomUUID();
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(file.getSize());
			s3client.putObject(new PutObjectRequest(bucket, filename, file.getInputStream(), meta));
			String url = s3client.getUrl(bucket, filename).toString();
			data.put("msg", null);
			data.put("url", url);
		} catch (IOException e) {
			data.put("msg", "파일 업로드 실패");
			data.put("url", null);
		} finally {
			return data;
		}
	}

	@Override
	@Transactional
	public Map deleteTeamImage(Integer teamSeq, String email) {
		Map<String, String> data = new HashMap<>();
		data.put("msg", null);
		Optional<DeveloperTeamEntity> team = developerTeamRepository.findById(teamSeq);
		if (team.isEmpty()) {
			data.put("msg", "팀을 찾을 수 없습니다.");
			return data;
		}
		// 요청한 사람의 시퀀스 아이디
		String myEmail = email;
		Integer mySeq =     memberRepository.findByEmail(myEmail).get().getMemberId();
		Optional<TeamMemberEntity> member = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if (member.isEmpty()) {
			data.put("msg", "사진 수정 권한이 없습니다.");
			return data;
		}
		DeveloperTeamEntity e = team.get();
		e.setServiceImage(null);
		developerTeamRepository.save(e);
		return data;
	}

	@Override
	@Transactional
	public Map modifyTeamImage(Integer teamSeq, TeamImageVo vo, String email) {
		Map<String, String> data = new HashMap<>();
		data.put("msg", null);
		Optional<DeveloperTeamEntity> team = developerTeamRepository.findById(teamSeq);
		if (team.isEmpty()) {
			data.put("msg", "팀을 찾을 수 없습니다.");
			return data;
		}
		// 요청한 사람의 시퀀스 아이디
		String myEmail = email;
		Integer mySeq =     memberRepository.findByEmail(myEmail).get().getMemberId();
		Optional<TeamMemberEntity> member = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if (member.isEmpty()) {
			data.put("msg", "사진 수정 권한이 없습니다.");
			return data;
		}
		DeveloperTeamEntity e = team.get();
		e.setServiceImage(vo.getUrl());
		developerTeamRepository.save(e);
		return data;
	}

	@Override
	@Transactional(readOnly = true)
	public Map listInvitedTeam(String email) {
		Map<String, Object> data = new HashMap<>();
		// 사용자 정보 가져오기
		String myEmail = email;
		Integer mySeq =     memberRepository.findByEmail(myEmail).get().getMemberId();
		// 사용자가 추가 되었는데 수락하지 않은 리스트 조회
		List<InviteListVo> list = teamListRepository.findByMemberSeqAndIsAcceptFalse(mySeq).stream().map(entity -> {
			InviteListVo vo = new InviteListVo();
			vo.setTeamSeq(entity.getTeamSeq());
			vo.setTeamName(entity.getTeamName());
			String leader = memberRepository.findById(entity.getLeader()).get().getName();
			vo.setLeader(leader);
			return vo;
		}).collect(Collectors.toList());
		if(list.isEmpty()){
			data.put("msg", "초대된 팀이 없습니다.");
			data.put("list", null);
		}else{
			data.put("msg", null);
			data.put("list", list);
		}
		return data;
	}

	@Override
	@Transactional
	public Map acceptInvite(Integer teamSeq, String email) {
		Map<String, Object> data = new HashMap<>();

		// 사용자 정보 가져오기
		String myEmail = email;
		Integer mySeq =     memberRepository.findByEmail(myEmail).get().getMemberId();

		// 초대 확인
		Optional<TeamMemberEntity> optional = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if(optional.isEmpty()){
			data.put("msg", "초대 된 적이 없습니다.");
			data.put("list", null);
			return data;
		}

		// 초대 수락
		TeamMemberEntity e = optional.get();
		e.setIsAccept(true);
		teamMemberRepository.save(e);

		// 팀 초대 리스트 재 조회
		List<InviteListVo> list = teamListRepository.findByMemberSeqAndIsAcceptFalse(mySeq).stream().map(entity -> {
			InviteListVo vo = new InviteListVo();
			vo.setTeamSeq(entity.getTeamSeq());
			vo.setTeamName(entity.getTeamName());
			String leader = memberRepository.findById(entity.getLeader()).get().getName();
			vo.setLeader(leader);
			return vo;
		}).collect(Collectors.toList());
		if(list.isEmpty()){
			data.put("msg", "초대된 팀이 없습니다.");
			data.put("list", null);
		}else{
			data.put("msg", null);
			data.put("list", list);
		}
		return data;
	}

	@Override
	@Transactional
	public Map rejectInvite(Integer teamSeq, String email) {
		Map<String, Object> data = new HashMap<>();

		// 사용자 정보 가져오기
		String myEmail = email;
		Integer mySeq = memberRepository.findByEmail(myEmail).get().getMemberId();

		// 초대 확인
		Optional<TeamMemberEntity> optional = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if(optional.isEmpty()){
			data.put("msg", "초대 된 적이 없습니다.");
			data.put("list", null);
			return data;
		}

		// 초대 삭제
		teamMemberRepository.delete(optional.get());

		// 팀 초대 리스트 재 조회
		List<InviteListVo> list = teamListRepository.findByMemberSeqAndIsAcceptFalse(mySeq).stream().map(entity -> {
			InviteListVo vo = new InviteListVo();
			vo.setTeamSeq(entity.getTeamSeq());
			vo.setTeamName(entity.getTeamName());
			String leader = memberRepository.findById(entity.getLeader()).get().getName();
			vo.setLeader(leader);
			return vo;
		}).collect(Collectors.toList());
		if(list.isEmpty()){
			data.put("msg", "초대된 팀이 없습니다.");
			data.put("list", null);
		}else{
			data.put("msg", null);
			data.put("list", list);
		}
		return data;
	}


	@Override
	public List<String> getBlockedCountriesByTeamId(Integer teamSeq) {
		Optional<BlockedCountriesEntity> blockedCountry = blockedCountriesRepository.findByTeamId(teamSeq);
		if(blockedCountry.isPresent()){
			BlockedCountriesEntity entity = blockedCountry.get();
			List<String> countryList = Arrays.asList(entity.getCountryList().split(","));
			return countryList;
		}
		return new ArrayList<>();
	}

	@Override
	public boolean updateBlockedCountries(List<String> countries,int teamSeq) {
		System.out.println("sssssssssssssssssssss" + countries);

		return false;
	}

	@Override
	public Map countServiceUser(Integer teamSeq) {
		Map<String, Integer> data = new HashMap<>();
		Integer userCount = oauth2AuthorizationConsentRepository.countServiceUser(String.valueOf(teamSeq));
		data.put("userCount", userCount);
		return data;
	}

	@Override
	public Map countLoginUser(Integer teamSeq) {
		Map<String, Long> data = new HashMap<>();
		CountLoginUserEntity countLoginUser = countLoginUserRepository.findByServiceId(String.valueOf(teamSeq));
		if (countLoginUser!=null) {
			data.put("userLoginCount", countLoginUser.getLoginCount());
		} else {
			data.put("userLoginCount", 0L);
		}
		return data;
	}

}

package com.ssafy.authorization.link.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.authorization.link.entity.CustomerEntity;
import com.ssafy.authorization.link.entity.SubscribeEntity;
import com.ssafy.authorization.link.entity.SubscribePK;
import com.ssafy.authorization.link.repository.CustomerRepository;
import com.ssafy.authorization.link.repository.SubscribeRepository;
import com.ssafy.authorization.link.vo.CustomerVo;
import com.ssafy.authorization.link.vo.LinkVo;
import com.ssafy.authorization.member.model.repository.MemberRepository;
import com.ssafy.authorization.team.entity.DeveloperTeamEntity;
import com.ssafy.authorization.team.entity.TeamMemberEntity;
import com.ssafy.authorization.team.entity.TeamMemberPK;
import com.ssafy.authorization.team.repository.DeveloperTeamRepository;
import com.ssafy.authorization.team.repository.TeamMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService{
	private final MemberRepository memberRepository;
	private final SubscribeRepository subscribeRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final DeveloperTeamRepository developerTeamRepository;
	private final CustomerRepository customerRepository;

	@Override
	@Transactional(readOnly = true)
	public Map listLink(Authentication authentication) {
		Map<String, Object> data = new HashMap<>();
		// 로그인한 사용자 정보 가져오기
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		String myEmail = userDetails.getUsername();
		Integer mySeq = (int)(long)memberRepository.findByEmail(myEmail).get().getMemberId();
		// 사용자의 가입한 서비스 가져오기
		List<SubscribeEntity> subscribes = subscribeRepository.findAllByMemberSeqAndIsDeleteFalse(mySeq);
		if(subscribes.isEmpty()){
			data.put("msg", "가입한 서비스가 없습니다.");
			data.put("list", null);
			return data;
		}
		List<LinkVo> list = subscribes.stream().map(entity->{
			LinkVo vo = new LinkVo();
			vo.setServiceName(entity.getSingedUpServices().getServiceName());
			vo.setServiceImage(entity.getSingedUpServices().getServiceImage());
			vo.setIsDelete(entity.getSingedUpServices().getIsDelete());
			vo.setTeamSeq(entity.getTeamSeq());
			return vo;
		}).collect(Collectors.toList());
		data.put("msg", null);
		data.put("list", list);
		return data;
	}

	@Override
	public Map removeLink(Integer teamSeq, Authentication authentication) {
		Map<String, Object> data = new HashMap<>();
		// 로그인한 사용자 정보 가져오기
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		String myEmail = userDetails.getUsername();
		Integer mySeq = (int)(long)memberRepository.findByEmail(myEmail).get().getMemberId();
		// 사용자가 가입한 서비스 가져오기
		Optional<SubscribeEntity> optional = subscribeRepository.findById(new SubscribePK(mySeq, teamSeq));
		if(optional.isEmpty()){
			data.put("msg", "가입한적 없는 서비스 입니다.");
		}else{
			data.put("msg", null);
			SubscribeEntity subscribeEntity = optional.get();
			subscribeEntity.setDeleteDate(LocalDateTime.now());
			subscribeEntity.setIsDelete(true);
			subscribeRepository.save(subscribeEntity);
		}
		List<SubscribeEntity> subscribes = subscribeRepository.findAllByMemberSeqAndIsDeleteFalse(mySeq);
		if(subscribes.isEmpty()){
			data.put("msg", "가입한 서비스가 없습니다.");
			data.put("list", null);
			return data;
		}
		List<LinkVo> list = subscribes.stream().map(entity->{
			LinkVo vo = new LinkVo();
			vo.setServiceName(entity.getSingedUpServices().getServiceName());
			vo.setServiceImage(entity.getSingedUpServices().getServiceImage());
			vo.setIsDelete(entity.getSingedUpServices().getIsDelete());
			vo.setTeamSeq(entity.getTeamSeq());
			return vo;
		}).collect(Collectors.toList());
		data.put("list", list);
		return data;
	}

	@Override
	public Map listCustomer(Integer teamSeq, Authentication authentication) {
		Map<String, Object> data = new HashMap<>();
		// 존재하는 팀인지 확인
		Optional<DeveloperTeamEntity> optionalDeveloperTeam = developerTeamRepository.findById(teamSeq);
		if(optionalDeveloperTeam.isEmpty()){
			data.put("msg", "존재하지 않는 서비스 입니다.");
			data.put("list", null);
			return data;
		}
		// 요청한 사람 확인
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		String myEmail = userDetails.getUsername();
		Integer mySeq = (int)(long)memberRepository.findByEmail(myEmail).get().getMemberId();
		// 요청한 사람이 팀의 팀원 인지 확인
		Optional<TeamMemberEntity> optionalTeamMember = teamMemberRepository.findById(new TeamMemberPK(teamSeq, mySeq));
		if(optionalTeamMember.isEmpty()){
			data.put("msg", "서비스에 가입된 사용자를 볼 수 있는 권한이 없습니다.");
			data.put("list", null);
			return data;
		}
		// 요청한 사람이 팀의 팀원 이면 리스트 출력
		List<CustomerEntity> customers = customerRepository.findAllByTeamSeq(teamSeq);
		// 고객이 없는 경우
		if(customers.isEmpty()){
			data.put("msg", "서비스에 가입된 사용자가 없습니다.");
			data.put("list", null);
			return data;
		}
		List<CustomerVo> list = customers.stream().map(e -> {
			CustomerVo vo = new CustomerVo();
			vo.setMemberSeq(e.getMemberSeq());
			vo.setName(e.getName());
			vo.setEmail(e.getEmail());
			vo.setImage(e.getImage());
			vo.setGender(e.getGender() == (short)0 ? false : true);
			vo.setTrack(e.getTrack());
			vo.setPhoneNumber(e.getPhoneNumber());
			vo.setStudentId(e.getStudentId());
			return vo;
		}).collect(Collectors.toList());
		data.put("msg", null);
		data.put("list", list);
		return data;
	}
}

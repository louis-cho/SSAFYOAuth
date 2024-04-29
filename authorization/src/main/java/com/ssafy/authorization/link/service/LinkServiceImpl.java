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

import com.ssafy.authorization.link.entity.SubscribeEntity;
import com.ssafy.authorization.link.entity.SubscribePK;
import com.ssafy.authorization.link.repository.SubscribeRepository;
import com.ssafy.authorization.link.vo.LinkVo;
import com.ssafy.authorization.member.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService{
	private final MemberRepository memberRepository;
	private final SubscribeRepository subscribeRepository;

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
}

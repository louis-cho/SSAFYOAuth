package com.ssafy.resourceserver.key.service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.ssafy.resourceserver.key.model.ServiceKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.resourceserver.key.model.domain.ServiceKey;
import com.ssafy.resourceserver.member.model.domain.Member;
import com.ssafy.resourceserver.member.model.repository.MemberRepository;
import com.ssafy.resourceserver.team.entity.DeveloperTeamEntity;
import com.ssafy.resourceserver.team.repository.DeveloperTeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceKeyServiceImpl implements ServiceKeyService {

	private final ServiceKeyRepository serviceKeyRepository;

	private final DeveloperTeamRepository developerTeamRepository;

	private final MemberRepository memberRepository;

	private static final Random random = new Random();

	@Override
	public boolean createServiceKey(Integer teamId, String userEmail){
		Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		if (!verifyLeader(teamId, member.getMemberId())) throw new IllegalArgumentException("팀원이 맞아?");
		DeveloperTeamEntity team = developerTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
		team.setServiceKey(generateUniqueServiceKey());
		developerTeamRepository.save(team);
		return true;
	}

	@Override
	public boolean reCreateServiceKey(Integer teamId, String userEmail) {
		Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		if (!verifyLeader(teamId, member.getMemberId())) throw new IllegalArgumentException("팀원이 맞아?");
		DeveloperTeamEntity team = developerTeamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
		team.setServiceKey(generateUniqueServiceKey());
		developerTeamRepository.save(team);
		return true;
	}


	private boolean verifyLeader(Integer teamId, Integer clientId) {
		// 나의 제안 : 이것을 팀원이 아니라 팀장인지 확인하는 절차를 거쳐야 하는 것이 아닌가?
		DeveloperTeamEntity team = developerTeamRepository.findBySeq(teamId);
		return team.getLeader() == clientId;
	}

	private String generateUniqueServiceKey() {
		String newServiceKey;
		boolean isKeyUnique;

		do {
			newServiceKey = UUID.randomUUID().toString();
			isKeyUnique = isServiceKeyUnique(newServiceKey);
		} while (!isKeyUnique);

		return newServiceKey;
	}

	private boolean isServiceKeyUnique(String key) {
		return !serviceKeyRepository.existsByKey(key);
	}


}

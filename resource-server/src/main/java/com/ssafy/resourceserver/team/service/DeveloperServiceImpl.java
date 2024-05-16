package com.ssafy.resourceserver.team.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ssafy.resourceserver.team.entity.DeveloperEntity;
import com.ssafy.resourceserver.team.repository.DeveloperRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService{

	private final DeveloperRepository developerRepository;

	@Override
	public void developerSignUp(Integer memberSeq) {
		developerRepository.save(
			DeveloperEntity.builder()
				.memberSeq(memberSeq)
				.createDate(LocalDateTime.now())
				.isDelete(false)
				.build()
		);
	}
}

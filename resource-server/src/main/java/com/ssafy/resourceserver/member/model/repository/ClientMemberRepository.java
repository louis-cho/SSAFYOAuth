package com.ssafy.resourceserver.member.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.resourceserver.team.entity.DeveloperEntity;
public interface ClientMemberRepository extends JpaRepository<DeveloperEntity,Integer> {
	int countByMemberSeq(Integer memberSeq);
}

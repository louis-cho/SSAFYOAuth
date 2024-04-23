package com.ssafy.authorization.team.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.team.entity.DeveloperMemberEntity;

@Repository
public interface DeveloperMemberRepository extends JpaRepository<DeveloperMemberEntity, Integer> {
	List<DeveloperMemberEntity> findAllByEmail(String email);
}

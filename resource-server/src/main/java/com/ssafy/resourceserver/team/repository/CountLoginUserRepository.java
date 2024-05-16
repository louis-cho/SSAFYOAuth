package com.ssafy.resourceserver.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.resourceserver.team.entity.CountLoginUserEntity;

public interface CountLoginUserRepository extends JpaRepository<CountLoginUserEntity, String> {
	CountLoginUserEntity findByServiceId(String teamSeq);
}

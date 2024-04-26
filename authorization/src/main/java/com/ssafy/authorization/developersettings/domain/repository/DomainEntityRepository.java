package com.ssafy.authorization.developersettings.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.authorization.developersettings.domain.model.DomainEntity;

public interface DomainEntityRepository extends JpaRepository<DomainEntity, Integer> {
	int countByTeamId(int teamId);
}

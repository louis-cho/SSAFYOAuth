package com.ssafy.authorization.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.authorization.team.entity.BlockedCountriesEntity;

public interface BlockedCountriesRepository extends JpaRepository<BlockedCountriesEntity,Integer> {
	Optional<BlockedCountriesEntity> findByTeamId(Integer teamId);

}

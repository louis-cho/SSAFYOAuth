package com.ssafy.resourceserver.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.resourceserver.team.entity.BlockedCountriesEntity;

public interface BlockedCountriesRepository extends JpaRepository<BlockedCountriesEntity,Integer> {
	Optional<BlockedCountriesEntity> findByTeamId(Integer teamId);

}

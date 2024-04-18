package com.ssafy.authorization.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.team.entity.DeveloperTeamEntity;

@Repository
public interface DeveloperTeamRepository extends JpaRepository<DeveloperTeamEntity, Integer> {
}

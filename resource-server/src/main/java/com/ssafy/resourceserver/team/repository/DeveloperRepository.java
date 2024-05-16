package com.ssafy.resourceserver.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.resourceserver.team.entity.DeveloperEntity;

public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Integer> {

}

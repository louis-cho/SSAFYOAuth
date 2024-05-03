package com.ssafy.resourceserver.redirect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.resourceserver.redirect.model.RedirectEntity;

public interface RedirectEntityRepository extends JpaRepository<RedirectEntity, Integer> {
	int countByTeamId(int teamId);

	List<RedirectEntity> findAllByTeamId(Integer teamId);
}

package com.ssafy.authorization.redirect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.authorization.redirect.model.RedirectEntity;

public interface RedirectEntityRepository extends JpaRepository<RedirectEntity, Integer> {
	int countByTeamId(int teamId);
}

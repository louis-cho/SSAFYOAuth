package com.ssafy.authorization.developersettings.redirect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.authorization.developersettings.redirect.model.RedirectEntity;

public interface RedirectEntityRepository extends JpaRepository<RedirectEntity, Integer> {
	int countByTeamId(int teamId);
}

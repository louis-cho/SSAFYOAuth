package com.ssafy.authorization.redirect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.authorization.redirect.model.RedirectEntity;

public interface RedirectEntityRepository extends JpaRepository<RedirectEntity, Integer> {
	int countByTeamId(int teamId);

	List<RedirectEntity> findAllByTeamId(Integer teamId);
}

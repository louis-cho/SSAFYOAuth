package com.ssafy.authorization.stats.login.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ssafy.authorization.stats.login.model.LoginDocument;

public interface LoginStatsRepository
	extends ElasticsearchRepository<LoginDocument, Long> {
	List<LoginDocument> findByUserId(String userId, String teamId);

	List<LoginDocument> findByTeamId(String teamId);
}
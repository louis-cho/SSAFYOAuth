package com.ssafy.authorization.stats.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import com.ssafy.authorization.stats.login.model.LoginDocument;
import com.ssafy.authorization.stats.login.repository.LoginStatsRepository;

@Service
public class LoginStatsServiceImpl implements LoginStatsService {

	private final LoginStatsRepository loginStatsRepository;
	private final ElasticsearchOperations elasticsearchOperations;

	@Autowired
	LoginStatsServiceImpl(LoginStatsRepository loginStatsRepository, ElasticsearchOperations elasticsearchOperations) {
		this.loginStatsRepository = loginStatsRepository;
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@Override
	public List<LoginDocument> findByUserId(String userId, String teamId) {

		return null;
	}

	@Override
	public List<LoginDocument> findByTeamId(String teamId) {
		return null;
	}

	@Override
	public int saveUserLogin(LoginDocument loginDocument) {
		try {
			elasticsearchOperations.save(loginDocument);
		} catch (Exception e) {
			return -1;
		}
		return 1;
	}
}
